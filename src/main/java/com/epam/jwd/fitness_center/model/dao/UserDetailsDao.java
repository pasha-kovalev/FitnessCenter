package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.math.BigDecimal;
import java.util.List;

/**
 * The interface represents user details dao
 */
public interface UserDetailsDao extends EntityDao<UserDetails> {

    boolean updateDiscountByRole(BigDecimal discount, UserRole role) throws DaoException;

    List<UserDetails> findByUserId(Long userId) throws DaoException;
}