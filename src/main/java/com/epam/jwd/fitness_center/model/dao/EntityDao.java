package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity> {

    T create(T entity) throws DaoException;

    List<T> read() throws DaoException;

    Optional<T> read(Long id) throws DaoException;

    boolean update(T entity) throws DaoException;

    boolean delete(Long id) throws DaoException;
}
