package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService extends EntityService<User> {

    Optional<User> register(String email, String password1, String password2, String firstName, String secondName,
                            UserRole role, UserStatus status, String locale) throws ServiceException;

    Optional<User> authenticate(String login, String password) throws ServiceException;

    Optional<User> findUserByEmail(String email) throws ServiceException;

    Optional<User> findUserById(Long id) throws ServiceException;

    void updateUserStatus(UserStatus status, long id) throws ServiceException;

    boolean isEmailRegistered(String email) throws ServiceException;

    Optional<User> findUserByTokenId(long tokenId) throws ServiceException;

    Optional<Token> retrieveUserToken(long tokenId) throws ServiceException;

    boolean confirmUser(long tokenId, String tokenValue) throws ServiceException;

    List<User> findActiveTrainers() throws ServiceException;

    Optional<UserDetails> findUserDetails(long userId) throws ServiceException;

    UserDetails addUserDetails(User user) throws ServiceException;

    void updateUserDetails(UserDetails userDetails) throws ServiceException;

    void updateUserDetailsDiscount(UserDetails userDetails, BigDecimal discount) throws ServiceException;

    void updateUserDetailsTrainerId(UserDetails userDetails, long trainerId) throws ServiceException;
}
