package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;

import java.util.List;
import java.util.Optional;

public interface OrderService extends EntityService<Order> {

    List<Order> findOrderByStatus(OrderStatus status) throws ServiceException;
    List<Order> findOrderByTrainerId(Long trainerId) throws ServiceException;
    List<Order> findOrderByAssignmentTrainerId(Long trainerId) throws ServiceException;
    void updateOrderStatus(OrderStatus status, long id) throws ServiceException;
    Order insert(long userDetailsId, OrderStatus status, long itemId, long trainerId, long period,
                 String comment) throws ServiceException;
}
