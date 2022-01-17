package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.OrderDao;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.util.TextEscapeUtil;
import com.epam.jwd.fitness_center.model.validator.OrderValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class);
    private final OrderDao orderDao;

    OrderServiceImpl() {
        orderDao = DaoProvider.getInstance().getOrderDao();
    }

    @Override
    public List<Order> findAll() throws ServiceException {
        try {
            return orderDao.read();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all orders", e);
        }
    }

    @Override
    public boolean update(Order entity) throws ServiceException {
        try {
            return orderDao.update(entity);
        } catch (DaoException e) {
            LOG.error("Unable to update order with id: {}. {}", entity.getId(), e.getMessage());
            throw new ServiceException("Unable to update order", e);
        }
    }

    @Override
    public Order insert(Order entity) throws ServiceException {
        try {
            return orderDao.create(entity);
        } catch (DaoException e) {
            LOG.error("Unable to insert order : {}. {}", entity, e.getMessage());
            throw new ServiceException("Unable to insert order", e);
        }
    }

    @Override
    public Order insert(long userDetailsId, OrderStatus status, long itemId, long trainerId, long period,
                        String comment) throws ServiceException {
        String commentEscaped = TextEscapeUtil.escapeHtml(comment);
        if (!OrderValidator.isValidPeriod(period)) {
            throw new ServiceException("Not valid period: " + period);
        }
        BigDecimal price = calcPrice(userDetailsId, itemId, period);
        Order order = new Order.Builder()
                .setUserDetailsId(userDetailsId)
                .setOrderStatus(status)
                .setItem(ServiceProvider.getInstance().getItemService().find(itemId).get())
                .setTrainerId(trainerId)
                .setPrice(price)
                .setComment(commentEscaped)
                .build();
        try {
            return orderDao.create(order);
        } catch (DaoException e) {
            LOG.error("Unable to insert order : {}. {}", order, e.getMessage());
            throw new ServiceException("Unable to insert order", e);
        }
    }

    private BigDecimal calcPrice(long userDetailsId, long itemId, long itemAmount) throws ServiceException {
        ItemServiceImpl itemService = ServiceProvider.getInstance().getItemService();
        return itemService.calculateItemPriceForUser(userDetailsId, itemId).multiply(new BigDecimal(itemAmount));
    }

    @Override
    public List<Order> findOrderByStatus(OrderStatus status) throws ServiceException {
        try {
            return orderDao.findByStatus(status);
        } catch (DaoException e) {
            LOG.error("Error during searching for order by status: {}. {}", status.name(), e.getMessage());
            throw new ServiceException("Error during searching for order by status", e);
        }
    }

    @Override
    public List<Order> findOrderByTrainerId(Long trainerId) throws ServiceException {
        try {
            return orderDao.findByTrainerId(trainerId);
        } catch (DaoException e) {
            LOG.error("Error during searching for order by trainerId: {}. {}", trainerId, e.getMessage());
            throw new ServiceException("Error during searching for order by trainerId", e);
        }
    }

    @Override
    public Optional<Order> findOrderById(Long id) throws ServiceException {
        try {
            return orderDao.read(id);
        } catch (DaoException e) {
            LOG.error("Error during searching for order by id: {}. {}", id, e.getMessage());
            throw new ServiceException("Error during searching for order by id", e);
        }
    }

    @Override
    public List<Order> findOrderByUserId(Long userId) throws ServiceException {
        try {
            return orderDao.findByUserId(userId);
        } catch (DaoException e) {
            LOG.error("Error during searching for order by userId: {}. {}", userId, e.getMessage());
            throw new ServiceException("Error during searching for order by userId", e);
        }
    }

    @Override
    public List<Order> findOrderByAssignmentTrainerId(Long trainerId) throws ServiceException {
        try {
            return orderDao.findByAssignmentTrainerId(trainerId);
        } catch (DaoException e) {
            LOG.error("Error during searching for order by assignmentTrainerId: {}. {}",
                    trainerId, e.getMessage());
            throw new ServiceException("Error during searching for order by assignmentTrainerId", e);
        }
    }

    @Override
    public List<Order> findOrderByAssignmentTrainerIdAndStatus(Long trainerId, OrderStatus status,
                                                               OrderStatus... statuses) throws ServiceException {
        return findOrderByAssignmentTrainerId(trainerId).stream()
                .filter(o -> o.getOrderStatus() == status
                        || Arrays.stream(statuses)
                                 .anyMatch(s -> s == o.getOrderStatus()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateOrderStatus(OrderStatus status, long id) throws ServiceException {
        try {
            orderDao.updateStatus(status, id);
        } catch (DaoException e) {
            LOG.error("Error during updating status of order with id : {}. {}", id, e.getMessage());
            throw new ServiceException("Error during updating order status by id", e);
        }
    }
}
