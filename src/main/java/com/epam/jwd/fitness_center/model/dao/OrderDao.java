package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import java.util.List;

public interface OrderDao extends EntityDao<Order> {
    boolean updateStatus(OrderStatus status, long id) throws DaoException;
    List<Order> findByStatus(OrderStatus status) throws DaoException;
    List<Order> findByTrainerId(Long trainerId) throws DaoException;
    List<Order> findByAssignmentTrainerId(Long trainerId) throws DaoException;
}