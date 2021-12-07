package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.dao.impl.ItemDaoImpl;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.service.EntityService;
import com.epam.jwd.fitness_center.model.validator.UserValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.stream.Collectors;

public class ItemServiceImpl implements EntityService<Item> {
    private static final Logger LOG = LogManager.getLogger(ItemServiceImpl.class);
    private static final int MONEY_PRECISION = 2;
    private static final int PERCENT_DIVISOR = 100;

    private final ItemDaoImpl itemDao;

    ItemServiceImpl() {
        itemDao = DaoProvider.getInstance().getItemDao();
    }

    @Override
    public List<Item> findAll() throws ServiceException {
        try {
            return itemDao.read();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all items", e);
        }
    }

    @Override
    public boolean update(Item entity) throws ServiceException {
        try {
            return itemDao.update(entity);
        } catch (DaoException e) {
            LOG.error("Unable to update item with id: {}. {}", entity.getId(), e.getMessage());
            throw new ServiceException("Unable to update user", e);
        }
    }

    @Override
    public Item insert(Item entity) throws ServiceException {
        try {
            return itemDao.create(entity);
        } catch (DaoException e) {
            LOG.error("Unable to insert item with name: {}. {}", entity.getName(), e.getMessage());
            throw new ServiceException("Unable to insert item", e);
        }
    }

    public List<Item> modifyItemsByDiscount(List<Item> items, BigDecimal discount) {
        return items.stream()
                .peek(i -> i.setPrice(calcFinalPrice(i.getPrice(), discount)))
                .collect(Collectors.toList());
    }


    private BigDecimal calcFinalPrice(BigDecimal price, BigDecimal discount) {
        return price.subtract(price.multiply(discount.divide(BigDecimal.valueOf(PERCENT_DIVISOR))),
                                             new MathContext(MONEY_PRECISION));
    }
}
