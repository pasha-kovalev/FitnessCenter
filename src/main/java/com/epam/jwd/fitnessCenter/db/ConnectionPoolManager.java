package com.epam.jwd.fitnessCenter.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolManager implements ConnectionPool {
    private static final Logger LOG = LogManager.getLogger(ConnectionPoolManager.class);

    private static final int INITIAL_POOL_SIZE = 4;
    private static final int MIN_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 8;

    //todo: get from property file in constructor
    private static final String DB_URL = "jdbc:mysql://localhost:3306/con1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "7510";

    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> usedConnections = new ArrayList<>();
    private boolean initialized = false;

    private ReentrantLock lock = new ReentrantLock(true);
    private Condition notAvailableConnection = lock.newCondition();

    private ConnectionPoolManager() {
        //todo: get bd info from property file.
    }

    private static class ConnectionPoolManagerHolder {
        private final static ConnectionPoolManager instance = new ConnectionPoolManager();
    }

    public static ConnectionPoolManager getInstance() {
        return ConnectionPoolManagerHolder.instance;
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (!initialized) {
                create(INITIAL_POOL_SIZE);
                initialized = true;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }


    @Override
    public boolean isInitialized() {
        return initialized;
    }

    //todo: ? why NMikle in initializeConnections(int amount, boolean failOnConnectionException)
    // use boolean parameter
    private void create(int amount) {
        try {
            for (int i = 0; i < amount; i++) {
                final Connection connection = DriverManager
                        .getConnection(DB_URL, DB_USER, DB_PASSWORD);
                final ProxyConnection proxyConnection = new ProxyConnection(connection, this);
                availableConnections.add(proxyConnection);
            }
        } catch (SQLException e) {
            LOG.error("Error during creating connection");
            //todo: throw your exception
        }

    }

    @Override
    public Connection takeConnection() throws InterruptedException {
        //todo: ? if waiting timeout expired
        // or is too long
        // -> check connections
        // or -> increase pool size
        lock.lock();
        try {
            while (availableConnections.isEmpty()) {
                notAvailableConnection.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            usedConnections.add(connection);
            return connection;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        lock.lock();
        try {
            //todo what if connecion NOT proxyConnection
            if (usedConnections.remove(connection)) {
                availableConnections.add((ProxyConnection) connection);
                notAvailableConnection.signalAll();
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean shutDown() {
        return false;
    }

}
