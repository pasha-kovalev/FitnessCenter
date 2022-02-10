package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents user dao
 */
public interface UserDao extends EntityDao<User> {
    /**
     * Updates user status
     *
     * @param status status
     * @param id     user id
     * @return true if updated
     * @throws DaoException when error while query execution occurs
     */
    boolean updateStatus(UserStatus status, long id) throws DaoException;

    /**
     * Finds user by email
     *
     * @param email email
     * @return optional with user or empty optional
     * @throws DaoException when error while query execution occurs
     */
    Optional<User> findByEmail(String email) throws DaoException;

    List<User> findActiveTrainers() throws DaoException;

    List<User> findActiveClients() throws DaoException;

    boolean updateRole(UserRole role, long id) throws DaoException;

}
