package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;

import java.util.List;

/**
 * The interface represents order dao
 */
public interface OrderDao extends EntityDao<Order> {
    /**
     * Updates order status in db
     *
     * @param status order status
     * @param id     order id
     * @return true if updated
     * @throws DaoException when error while query execution occurs
     */
    boolean updateStatus(OrderStatus status, long id) throws DaoException;

    /**
     * Selects all orders with given status
     *
     * @param status order status
     * @return list of found orders
     * @throws DaoException when error while query execution occurs
     */
    List<Order> findByStatus(OrderStatus status) throws DaoException;

    /**
     * Selects all orders with given trainer id
     *
     * @param trainerId trainer id
     * @return list of found orders
     * @throws DaoException when error while query execution occurs
     */
    List<Order> findByTrainerId(Long trainerId) throws DaoException;

    /**
     * Selects all orders with given user id
     *
     * @param userId user id
     * @return list of found orders
     * @throws DaoException when error while query execution occurs
     */
    List<Order> findByUserId(Long userId) throws DaoException;

    /**
     * Selects all orders with given assignment trainer id
     *
     * @param trainerId assignment trainer id
     * @return list of found orders
     * @throws DaoException when error while query execution occurs
     */
    List<Order> findByAssignmentTrainerId(Long trainerId) throws DaoException;

    List<Order> findActiveByUserId(Long userId) throws DaoException;
}