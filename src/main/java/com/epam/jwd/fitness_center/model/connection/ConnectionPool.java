package com.epam.jwd.fitness_center.model.connection;

import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;

import java.sql.Connection;

public interface ConnectionPool {
    boolean init();

    boolean isInitialized();

    Connection takeConnection() throws DatabaseConnectionException;

    boolean releaseConnection(Connection connection);

    boolean shutDown();
}
