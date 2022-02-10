package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Entity;

import java.util.List;

/**
 * The interface represents entity service
 */
public interface EntityService<T extends Entity> {
    /**
     * Finds all entities
     *
     * @return list of found entities
     * @throws ServiceException while unable to perform a service action
     */
    List<T> findAll() throws ServiceException;

    /**
     * Updates entity
     *
     * @param entity entity
     * @return true if success
     * @throws ServiceException while unable to perform a service action
     */
    boolean update(T entity) throws ServiceException;

    /**
     * Inserts entity
     *
     * @param entity entity
     * @return inserted entity
     * @throws ServiceException while unable to perform a service action
     */
    T insert(T entity) throws ServiceException;
}
