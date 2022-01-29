package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.Entity;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents entity dao
 */
public interface EntityDao<T extends Entity> {
    
    /**Inserts new entity
     * @param entity entity
     * @return inserted entity
     * @throws DaoException when error while query execution occurs or cannot insert new entity
     */
    T create(T entity) throws DaoException;

    /**Selects all entities
     *
     * @return list of found entities
     * @throws DaoException when error while query execution occurs
     */
    List<T> read() throws DaoException;

    /**Finds entity by id
     *
     * @param id entity id
     * @return optional with entity or empty optional
     * @throws DaoException when error while query execution occurs
     */
    Optional<T> read(Long id) throws DaoException;

    /**Updates entity in db by entity
     * @param entity entity
     * @return true if updated
     * @throws DaoException when error while query execution occurs
     */
    boolean update(T entity) throws DaoException;

    /**Deletes entity by id
     * @param id entity id
     * @return true if deleting was successful
     * @throws DaoException when error while query execution occurs
     */
    boolean delete(Long id) throws DaoException;
}
