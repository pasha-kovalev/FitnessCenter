package com.epam.jwd.fitness_center.model.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.OrderDao;
import com.epam.jwd.fitness_center.model.dao.TokenDao;
import com.epam.jwd.fitness_center.model.dao.UserDao;
import com.epam.jwd.fitness_center.model.dao.UserDetailsDao;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.MailService;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.util.TextEscapeUtil;
import com.epam.jwd.fitness_center.model.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;

public class UserServiceImpl implements UserService {
    public static final int TOKEN_EXPIRATION_DAYS = 1;
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    private static final String FIRSTNAME_FIELD_NAME = "firstname";
    private static final String LASTNAME_FIELD_NAME = "lastname";
    private static final String DESCRIPTION_FIELD_NAME = "description";

    private final UserDao userDao;
    private final BCrypt.Hasher hasher = BCrypt.withDefaults();
    private final BCrypt.Verifyer verifier = BCrypt.verifyer();

    UserServiceImpl() {
        userDao = DaoProvider.getInstance().getUserDao();
    }

    @Override
    public List<User> findAll() throws ServiceException {
        try {
            return userDao.read();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all users", e);
        }
    }

    @Override
    public boolean update(User user) throws ServiceException {
        try {
            return UserValidator.isValidUser(user, false) && userDao.update(user);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update user", e);
        }
    }

    @Override
    public User insert(User user) throws ServiceException {
        try {
            final char[] rawPassword = user.getPassword().toCharArray();
            final String hashedPassword = hasher.hashToString(MIN_COST, rawPassword);
            user.setPassword(hashedPassword);
            return userDao.create(user);
        } catch (DaoException e) {
            throw new ServiceException("Unable to insert user", e);
        }
    }

    @Override
    public Optional<User> register(String email, String password1, String password2, String firstName, String secondName,
                                   UserRole role, UserStatus status, String locale) throws ServiceException {
        if (!UserValidator.isValidName(firstName) || !UserValidator.isValidName(secondName)
                || !UserValidator.isValidEmail(email) || !UserValidator.isValidPassword(password1)
                || !UserValidator.isValidPassword(password2) || !UserValidator.isEqualPasswords(password1, password2)) {
            return Optional.empty();
        }
        if (isEmailRegistered(email)) return Optional.empty();
        User user = new User.Builder().setEmail(email)
                .setPassword(password1)
                .setFirstName(firstName)
                .setRole(role)
                .setSecondName(secondName)
                .setStatus(status).build();
        User userFromDb = insert(user);
        this.addUserDetails(userFromDb);
        long userId = userFromDb.getId();
        MailService mailService = ServiceProvider.getInstance().getMailService();
        mailService.sendConfirmationEmail(userId, email, locale);
        return Optional.of(userFromDb);
    }

    @Override
    public Optional<User> authenticate(String email, String password) throws ServiceException {
        if (!UserValidator.isValidEmail(email)) {
            return Optional.empty();
        }
        final byte[] enteredPassword = password.getBytes(StandardCharsets.UTF_8);
        final Optional<User> readUser = findUserByEmail(email);
        if (readUser.isPresent() && readUser.get().getStatus() != UserStatus.UNCONFIRMED) {
            final byte[] hashedPassword = readUser.get()
                    .getPassword()
                    .getBytes(StandardCharsets.UTF_8);
            return verifier.verify(enteredPassword, hashedPassword).verified
                    ? readUser
                    : Optional.empty();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws ServiceException {
        try {
            return userDao.findByEmail(email);
        } catch (DaoException e) {
            throw new ServiceException("Error during searching for user by email", e);
        }
    }

    @Override
    public Optional<User> findUserById(Long id) throws ServiceException {
        try {
            return userDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException("Error during searching for user by id", e);
        }
    }

    @Override
    public void updateUserStatus(UserStatus status, long id) throws ServiceException {
        try {
            userDao.updateStatus(status, id);
        } catch (DaoException e) {
            throw new ServiceException("Error during updating user status by id", e);
        }
    }

    @Override
    public void updateUserRole(UserRole role, long id) throws ServiceException {
        try {
            userDao.updateRole(role, id);
        } catch (DaoException e) {
            throw new ServiceException("Error during updating user role by id", e);
        }
    }

    @Override
    public boolean isEmailRegistered(String email) throws ServiceException {
        Optional<User> optionalUser = findUserByEmail(email);
        return optionalUser.isPresent() && hasValidToken(optionalUser.get());
    }

    private boolean hasValidToken(User user) {
        if (user.getStatus() == UserStatus.UNCONFIRMED) {
            try {
                TokenDao tokenDao = DaoProvider.getInstance().getTokenDao();
                tokenDao.removeExpiredToken(TOKEN_EXPIRATION_DAYS);
                if (!tokenDao.findByUserId(user.getId()).isEmpty()) {
                    return true;
                } else {
                    userDao.delete(user.getId());
                    return false;
                }
            } catch (DaoException e) {
                LOG.error("Error during updating tokenDao", e);
            }
        }
        return true;
    }

    @Override
    public boolean deleteUser(long userId) throws ServiceException {
        OrderDao orderDao = DaoProvider.getInstance().getOrderDao();
        try {
            List<Order> activeByUserId = orderDao.findActiveByUserId(userId);
            if (activeByUserId.isEmpty()) {
                userDao.delete(userId);
                return true;
            } else {
                return false;
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Optional<User> findUserByTokenId(long tokenId) throws ServiceException {
        Optional<Token> optionalToken = retrieveUserToken(tokenId);
        return optionalToken.isPresent() ? findUserById(optionalToken.get().getUserId()) : Optional.empty();
    }

    @Override
    public boolean confirmUser(long tokenId, String tokenValue) throws ServiceException {
        final Optional<Token> optionalToken = retrieveUserToken(tokenId);
        tokenValue = TextEscapeUtil.escapeHtml(tokenValue);
        if (!optionalToken.isPresent()) {
            LOG.error("Unable to confirm user because token not found. Token ID: {}", tokenId);
            return false;
        }
        Token token = optionalToken.get();
        Optional<User> optionalUser = findUserByTokenId(tokenId);
        if (optionalUser.isPresent() && optionalUser.get().getStatus() == UserStatus.UNCONFIRMED
                && checkToken(token, tokenValue)) {
            updateUserStatus(UserStatus.INACTIVE, token.getUserId());
            removeUserTokens(optionalUser.get().getId());
            return true;
        }
        return false;
    }

    private boolean checkToken(Token token, String value) {
        return token.getValue().equals(value) &&
                token.getCreationDate().isBefore(LocalDateTime.now().plusDays(TOKEN_EXPIRATION_DAYS));
    }


    private void removeUserTokens(Long userId) {
        final TokenDao tokenDao = DaoProvider.getInstance().getTokenDao();
        try {
            tokenDao.removeByUserId(userId);
        } catch (DaoException e) {
            LOG.error("Unable to remove tokens by user id : {}. {}", userId, e.getMessage());
        }
    }

    @Override
    public Optional<Token> retrieveUserToken(long tokenId) throws ServiceException {
        final TokenDao tokenDao = DaoProvider.getInstance().getTokenDao();
        final Optional<Token> token;
        try {
            token = tokenDao.read(tokenId);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find token", e);
        }

        return token;
    }

    @Override
    public List<User> findActiveTrainers() throws ServiceException {
        try {
            return userDao.findActiveTrainers();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all active trainers", e);
        }
    }

    @Override
    public List<User> findActiveClients() throws ServiceException {
        try {
            return userDao.findActiveClients();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all active clients", e);
        }
    }

    @Override
    public List<UserDetails> findAllUserDetails() throws ServiceException {
        final UserDetailsDao userDetailsDao = DaoProvider.getInstance().getUserDetailsDao();
        try {
            return userDetailsDao.read();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all user details", e);
        }
    }

    @Override
    public UserDetails findUserDetails(long userId) throws ServiceException {
        final UserDetailsDao userDetailsDao = DaoProvider.getInstance().getUserDetailsDao();
        List<UserDetails> userDetailsList;
        try {
            userDetailsList = userDetailsDao.findByUserId(userId);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find user details by id", e);
        }
        if (userDetailsList.isEmpty()) {
            throw new ServiceException("Unable to find user details by id");
        }
        return userDetailsList.get(0);
    }

    @Override
    public UserDetails addUserDetails(User user)
            throws ServiceException {
        final UserDetailsDao userDetailsDao = DaoProvider.getInstance().getUserDetailsDao();
        UserDetails userDetails = new UserDetails(user.getId(), BigDecimal.ZERO, null);
        try {
            return userDetailsDao.create(userDetails);
        } catch (DaoException e) {
            throw new ServiceException("Unable to add user details ", e);
        }
    }

    @Override
    public void updateUserDetails(UserDetails userDetails) throws ServiceException {
        try {
            final UserDetailsDao userDetailsDao = DaoProvider.getInstance().getUserDetailsDao();
            userDetailsDao.update(userDetails);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update user details", e);
        }
    }

    @Override
    public void updateUserData(long id, String fieldName, String value) throws ServiceException {
        Optional<User> optionalUser = findUserById(id);
        if (!optionalUser.isPresent()) {
            throw new ServiceException("Unable to update user. User not found by id");
        }
        User user = optionalUser.get();
        value = TextEscapeUtil.escapeHtml(value);
        fieldName = TextEscapeUtil.escapeHtml(fieldName);
        switch (fieldName.toLowerCase()) {
            case FIRSTNAME_FIELD_NAME:
                user.setFirstName(value);
                break;
            case LASTNAME_FIELD_NAME:
                user.setSecondName(value);
                break;
            case DESCRIPTION_FIELD_NAME:
                user.setDescription(value);
                break;
            default:
                throw new ServiceException("Unable to update user. Field name not correct");
        }
        try {
            userDao.update(user);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update user", e);
        }
    }

    @Override
    public void updateUserDetailsDiscount(long userId, String discount) throws ServiceException {
        BigDecimal discountNumber = new BigDecimal(TextEscapeUtil.escapeHtml(discount));
        UserDetails userDetails = findUserDetails(userId);
        userDetails.setDiscount(discountNumber);
        updateUserDetails(userDetails);
    }

    @Override
    public void updateUserDiscountByRole(long userId, String discount) throws ServiceException {
        final UserDetailsDao userDetailsDao = DaoProvider.getInstance().getUserDetailsDao();
        BigDecimal discountNumber = new BigDecimal(TextEscapeUtil.escapeHtml(discount));
        UserRole role = findUserById(userId)
                .orElseThrow(() -> new ServiceException("User not found. ID: " + userId))
                .getRole();
        try {
            userDetailsDao.updateDiscountByRole(discountNumber, role);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update user discount", e);
        }
    }

    @Override
    public void updateUserDetailsTrainerId(UserDetails userDetails, long trainerId) throws ServiceException {
        userDetails.setPersonalTrainerId(trainerId);
        updateUserDetails(userDetails);
    }
}
