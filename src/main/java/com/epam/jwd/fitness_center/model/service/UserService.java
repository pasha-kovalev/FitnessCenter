package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Trainer;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;

import java.util.Optional;

public interface UserService extends EntityService<User> {

    User register(String email, String password, String firstName, String secondName,
                  UserRole role, UserStatus status) throws ServiceException;

    Optional<User> authenticate(String login, String password) throws ServiceException;
    Optional<User> findUserByEmail(String email) throws ServiceException;
    Optional<User> findUserById(Long id) throws ServiceException;
    void updateUserStatus(UserStatus status, long id) throws ServiceException;
    boolean isEmailRegistered(String email) throws ServiceException;
    void confirmUser(long userId) throws ServiceException;
    Optional<Trainer> findActiveTrainers() throws ServiceException;
}