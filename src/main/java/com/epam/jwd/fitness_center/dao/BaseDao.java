package com.epam.jwd.fitness_center.dao;

import com.epam.jwd.fitness_center.dao.Entity;
import com.epam.jwd.fitness_center.exception.DaoException;

import java.util.List;

public interface BaseDao <T extends Entity> {
    List<T> findAll() throws DaoException;
    T findEntityById(Long id) throws DaoException;
    T update(T t) throws DaoException;
}
