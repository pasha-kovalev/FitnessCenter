package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * The interface represents order service
 */
public interface OrderService extends EntityService<Order> {
    /**
     * Finds all orders with given status
     *
     * @param status order status
     * @return list of found orders
     * @throws ServiceException while unable to perform a service action
     */
    List<Order> findOrderByStatus(OrderStatus status) throws ServiceException;

    /**
     * Finds all orders with given user id
     *
     * @param userId user id
     * @return list of found orders
     * @throws ServiceException while unable to perform a service action
     */
    List<Order> findOrderByUserId(Long userId) throws ServiceException;

    /**
     * Finds order by id
     *
     * @param id order id
     * @return optional with order or optional empty
     * @throws ServiceException while unable to perform a service action
     */
    Optional<Order> findOrderById(Long id) throws ServiceException;

    /**
     * Finds all orders with given assignment trainer id
     *
     * @param trainerId assignment trainer id
     * @return list of found orders
     * @throws ServiceException while unable to perform a service action
     */
    List<Order> findOrderByAssignmentTrainerId(Long trainerId) throws ServiceException;

    /**
     * Finds all orders with given assignment trainer id and order status(-es)
     *
     * @param trainerId assignment trainer id
     * @param status    order status
     * @param statuses  order statuses
     * @return list of found orders
     * @throws ServiceException while unable to perform a service action
     */
    List<Order> findOrderByAssignmentTrainerIdAndStatus(Long trainerId, OrderStatus status, OrderStatus... statuses)
            throws ServiceException;

    /**
     * Finds all orders with given user id and order status(-es)
     *
     * @param userId   user id
     * @param status   order status
     * @param statuses order statuses
     * @return list of found orders
     * @throws ServiceException while unable to perform a service action
     */
    List<Order> findOrderByUserIdAndStatus(Long userId, OrderStatus status, OrderStatus... statuses)
            throws ServiceException;

    /**
     * Updates order status
     *
     * @param status order status
     * @param id     order id
     * @throws ServiceException while unable to perform a service action
     */
    void updateOrderStatus(OrderStatus status, long id) throws ServiceException;

    /**
     * Adds review to order
     *
     * @param review review
     * @param id     order id
     * @return true if added
     * @throws ServiceException while unable to perform a service action
     */
    boolean addOrderReview(String review, long id) throws ServiceException;

    /**
     * Inserts new orders
     *
     * @param userDetailsId user details id
     * @param status        order status
     * @param itemId        item id
     * @param trainerId     trainer id
     * @param period        order period
     * @param comment       order comment
     * @return inserted order
     * @throws ServiceException while unable to perform a service action
     */
    Order insert(long userDetailsId, OrderStatus status, long itemId, long trainerId, long period,
                 String comment) throws ServiceException;

    boolean delete(long orderId) throws ServiceException;
}
