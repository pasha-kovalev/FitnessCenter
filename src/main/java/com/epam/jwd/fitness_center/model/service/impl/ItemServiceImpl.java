package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.dao.impl.ItemDaoImpl;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.service.EntityService;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.util.TextEscapeUtil;
import com.epam.jwd.fitness_center.model.validator.ItemValidator;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//todo add interface also for dao
public class ItemServiceImpl implements EntityService<Item> {
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

    public void changeIsArchive(long id) throws ServiceException {
        Optional<Item> optionalItem = find(id);
        Item item = optionalItem.orElseThrow(() -> new ServiceException("Item not found. Id: " + id));
        item.setIsArchive(!item.getIsArchive());
        update(item);
    }

    public Optional<Item> find(long id) throws ServiceException {
        try {
            return itemDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find item by id", e);
        }
    }

    @Override
    public boolean update(Item entity) throws ServiceException {
        try {
            return itemDao.update(entity);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update item", e);
        }
    }

    public boolean update(Long id, String name, String price, String description) throws ServiceException {
        Optional<Item> optionalItem = find(id);
        if (!optionalItem.isPresent()) {
            return false;
        }
        Item item = optionalItem.get();
        name = TextEscapeUtil.escapeHtml(name);
        description = TextEscapeUtil.escapeHtml(description);
        BigDecimal priceNumber = BigDecimal.valueOf(Double.parseDouble(TextEscapeUtil.escapeHtml(price)));
        item.setName(name);
        item.setPrice(priceNumber);
        item.setDescription(description);
        if (!ItemValidator.isValidItem(item)) {
            throw new ServiceException("Item is not valid");
        }
        try {
            return itemDao.update(item);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update item", e);
        }
    }

    public Item insert(String name, String price, String description) throws ServiceException {
        name = TextEscapeUtil.escapeHtml(name);
        description = TextEscapeUtil.escapeHtml(description);
        BigDecimal priceNumber = new BigDecimal(TextEscapeUtil.escapeHtml(price));
        Item item = new Item(name, priceNumber, description);
        if (!ItemValidator.isValidItem(item)) {
            throw new ServiceException("Item is not valid");
        }
        try {
            return itemDao.create(item);
        } catch (DaoException e) {
            throw new ServiceException("Unable to insert item", e);
        }
    }

    @Override
    public Item insert(Item entity) throws ServiceException {
        try {
            return itemDao.create(entity);
        } catch (DaoException e) {
            throw new ServiceException("Unable to insert item", e);
        }
    }


    public BigDecimal calculateItemPriceForUser(long userId, long itemId) throws ServiceException {
        UserService userService = ServiceProvider.getInstance().getUserService();
        userService.findUserDetails(userId);
        UserDetails userDetails = userService.findUserDetails(userId);
        Optional<Item> optionalItem = find(itemId);
        if (!optionalItem.isPresent()) {
            throw new ServiceException("Unable to calculate price. User ID: " + userId + " Item ID: " + itemId);
        }
        return calcFinalPrice(optionalItem.get().getPrice(), userDetails.getDiscount());
    }

    public List<Item> modifyItemsByDiscount(List<Item> items, BigDecimal discount) {
        return items.stream()
                .peek(i -> i.setPrice(calcFinalPrice(i.getPrice(), discount)))
                .collect(Collectors.toList());
    }


    private BigDecimal calcFinalPrice(BigDecimal price, BigDecimal discount) {
        return discount == null ? price
                : price.subtract(price.multiply(discount.divide(BigDecimal.valueOf(PERCENT_DIVISOR))),
                new MathContext(MONEY_PRECISION)).setScale(MONEY_PRECISION);
    }
}
