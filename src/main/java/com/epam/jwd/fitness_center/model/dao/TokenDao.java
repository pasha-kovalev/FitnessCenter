package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.Token;

import java.util.List;

/**
 * The interface represents order dao
 */
public interface TokenDao extends EntityDao<Token> {

    long removeByUserId(Long userId) throws DaoException;

    List<Token> findByUserId(Long userId) throws DaoException;

    void removeExpiredToken(int days) throws DaoException;
}