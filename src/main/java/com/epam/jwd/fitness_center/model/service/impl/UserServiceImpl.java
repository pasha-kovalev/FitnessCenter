package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.dao.impl.UserDaoImpl;
import com.epam.jwd.fitness_center.model.entity.Trainer;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);
    private final UserDaoImpl userDao;

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
    public boolean update(User entity) throws ServiceException {
        try {
            return userDao.update(entity);
        } catch (DaoException e) {
            LOG.error("Unable to update user with id: {}", entity.getId(), e);
            throw new ServiceException("Unable to update user", e);
        }
    }

    @Override
    public User insert(User entity) throws ServiceException {
        try {
            return userDao.create(entity);
        } catch (DaoException e) {
            LOG.error("Unable to insert user with email: {}", entity.getEmail(), e);
            throw new ServiceException("Unable to insert user", e);
        }
    }

    @Override
    public User register(String email, String password, String firstName, String secondName,
                         UserRole role, UserStatus status) throws ServiceException {
        User user = new User(email, password, firstName, secondName, role, status);
        return insert(user);
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        //todo
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws ServiceException {
        try {
            return userDao.findByEmail(email);
        } catch (DaoException e) {
            LOG.error("Error during searching for user by email: {}", email, e);
            throw new ServiceException("Error during searching for user by email", e);
        }
    }

    @Override
    public Optional<User> findUserById(Long id) throws ServiceException {
        try {
            return userDao.read(id);
        } catch (DaoException e) {
            LOG.error("Error during searching for user by id: {}", id, e);
            throw new ServiceException("Error during searching for user by id", e);
        }
    }

    @Override
    public void updateUserStatus(UserStatus status, long id) throws ServiceException {
        try {
            userDao.updateStatus(status, id);
        } catch (DaoException e) {
            LOG.error("Error during updating status of user with id : {}", id, e);
            throw new ServiceException("Error during updating user status by id", e);
        }
    }

    @Override
    public boolean isEmailRegistered(String email) throws ServiceException {
            return findUserByEmail(email).isPresent();
    }

    @Override
    public void confirmUser(long id) throws ServiceException {
        if (!findUserById(id).isPresent()) {
            LOG.error("Unable to confirm user because user not found. ID: {}", id);
            throw new ServiceException("Unable to confirm user because user not found. ID: " + id);
        }
        updateUserStatus(UserStatus.ACTIVE, id);
    }

    @Override
    public Optional<Trainer> findActiveTrainers() throws ServiceException {
        //todo add nullable description and photo path to {user table} for trainers
        return Optional.empty();
    }
}
