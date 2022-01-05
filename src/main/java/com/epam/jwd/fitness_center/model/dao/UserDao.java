package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserStatus;

import java.util.Optional;

public interface UserDao extends EntityDao<User> {
    boolean updateStatus(UserStatus status, long id) throws DaoException;

    Optional<User> findByEmail(String email) throws DaoException;
}
