package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * The interface represents item service
 */
public interface ItemService extends EntityService<Item> {

    void changeIsArchive(long id) throws ServiceException;

    Optional<Item> find(long id) throws ServiceException;

    boolean update(Long id, String name, String price, String description) throws ServiceException;

    Item insert(String name, String price, String description) throws ServiceException;

    BigDecimal calculateItemPriceForUser(long userId, long itemId) throws ServiceException;

    List<Item> modifyItemsByDiscount(List<Item> items, BigDecimal discount);
}
