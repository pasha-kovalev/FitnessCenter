package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.model.connection.ConnectionPoolManager;

public class DaoProvider {
    private final UserDaoImpl userDao = new UserDaoImpl(ConnectionPoolManager.getInstance());
    private final TokenDaoImpl tokenDao = new TokenDaoImpl(ConnectionPoolManager.getInstance());
    private final UserDetailsDaoImpl userDetailsDao = new UserDetailsDaoImpl(ConnectionPoolManager.getInstance());
    private final ItemDaoImpl itemDao = new ItemDaoImpl(ConnectionPoolManager.getInstance());


    private DaoProvider() {
    }

    public static DaoProvider getInstance() {
        return DaoProviderHolder.instance;
    }

    public UserDaoImpl getUserDao() {
        return userDao;
    }
    public TokenDaoImpl getTokenDao() {
        return tokenDao;
    }

    public UserDetailsDaoImpl getUserDetailsDao() {
        return userDetailsDao;
    }

    public ItemDaoImpl getItemDao() {
        return itemDao;
    }


    private static class DaoProviderHolder {
        private static final DaoProvider instance = new DaoProvider();
    }
}
