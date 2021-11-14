package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.model.connection.ConnectionPoolManager;

public class DaoProvider {
    private final UserDaoImpl userDao = new UserDaoImpl(ConnectionPoolManager.getInstance());

    private DaoProvider() {
    }

    public static DaoProvider getInstance() {
        return DaoProviderHolder.instance;
    }

    public UserDaoImpl getUserDao() {
        return userDao;
    }

    private static class DaoProviderHolder {
        private static final DaoProvider instance = new DaoProvider();
    }
}
