package com.epam.jwd.fitnessCenter.db;

import java.sql.Connection;

public interface ConnectionPool {

    boolean init();

    boolean isInitialized();

    Connection takeConnection() throws InterruptedException;

    boolean releaseConnection(Connection connection);

    boolean shutDown();


}
