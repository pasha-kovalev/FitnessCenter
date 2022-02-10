package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.model.connection.ConnectionPoolManager;
import com.epam.jwd.fitness_center.model.dao.*;

public class DaoProvider {
    private final UserDao userDao = new UserDaoImpl(ConnectionPoolManager.getInstance());
    private final TokenDao tokenDao = new TokenDaoImpl(ConnectionPoolManager.getInstance());
    private final UserDetailsDao userDetailsDao = new UserDetailsDaoImpl(ConnectionPoolManager.getInstance());
    private final ItemDaoImpl itemDao = new ItemDaoImpl(ConnectionPoolManager.getInstance());
    private final OrderDao orderDao = new OrderDaoImpl(ConnectionPoolManager.getInstance());
    private final CardDao cardDao = new CardDaoImpl(ConnectionPoolManager.getInstance());

    private final ProgramDaoImpl programDao = new ProgramDaoImpl(ConnectionPoolManager.getInstance());

    private DaoProvider() {
    }

    public static DaoProvider getInstance() {
        return DaoProviderHolder.instance;
    }

    public ProgramDaoImpl getProgramDao() {
        return programDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public TokenDao getTokenDao() {
        return tokenDao;
    }

    public UserDetailsDao getUserDetailsDao() {
        return userDetailsDao;
    }

    public ItemDaoImpl getItemDao() {
        return itemDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public CardDao getCardDao() {
        return cardDao;
    }

    private static class DaoProviderHolder {
        private static final DaoProvider instance = new DaoProvider();
    }
}
