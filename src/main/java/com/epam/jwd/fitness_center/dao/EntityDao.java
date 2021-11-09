package com.epam.jwd.fitness_center.dao;

import com.epam.jwd.fitness_center.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface EntityDao<T extends Entity> {
    T create(T entity);
    List<T> read() throws DaoException;
    Optional<T> read(Long id) throws DaoException;
    T update(T entity) throws DaoException;
    boolean delete(Long id);
}
