package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.model.connection.ConnectionPoolManager;

public class DaoProvider {
    private final UserDaoImpl userDao = new UserDaoImpl(ConnectionPoolManager.getInstance());
    private final TokenDaoImpl tokenDao = new TokenDaoImpl(ConnectionPoolManager.getInstance());

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

    private static class DaoProviderHolder {
        private static final DaoProvider instance = new DaoProvider();
    }
}
