package com.epam.jwd.fitness_center.db;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolManager implements ConnectionPool {
    private static final Logger LOG = LogManager.getLogger(ConnectionPoolManager.class);

    public static final int INITIAL_POOL_SIZE = 5;
    public static final int MIN_POOL_SIZE = 4;
    public static final int MAX_POOL_SIZE = 8;

    //todo ? should be encrypted
    private static final String CONFIG_PATH = "/config.properties";
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    private static Properties props;

    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> usedConnections = new ArrayList<>();

    private boolean initialized = false;

    private final ReentrantLock lock = new ReentrantLock(true);
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();
    private final Condition hasAvailableConnections = writeLock.newCondition();

    static {
        InputStream is;
        try {
            is = ClassLoader.class.getResourceAsStream(CONFIG_PATH);
            props = new Properties();
            props.load(is);
            LOG.info("DB property file load success");
        } catch (FileNotFoundException e) {
            LOG.fatal("Data base Property file not found", e);
            System.exit(-1);
        } catch (IOException e) {
            LOG.fatal("Unable to open data base property file", e);
            System.exit(-1);
        }

        DB_URL = props.getProperty("db.host");
        DB_USER = props.getProperty("db.login");
        DB_PASSWORD = props.getProperty("db.password");

    }


    private ConnectionPoolManager() {
    }

    private static class ConnectionPoolManagerHolder {
        private static final ConnectionPoolManager instance = new ConnectionPoolManager();
    }

    public static ConnectionPoolManager getInstance() {
        return ConnectionPoolManagerHolder.instance;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    public int getNumOfConnections() {
        readLock.lock();
        try {
            return availableConnections.size() + usedConnections.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (!initialized) {
                initializeConnections();
                initialized = true;
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    //todo: ? why NMikle in initializeConnections(int amount, boolean failOnConnectionException)
    // use boolean parameter
    private void initializeConnections() {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            addConnection();
        }
    }

    private boolean addConnection() {
        writeLock.lock();
        try {
            if (getNumOfConnections() < MAX_POOL_SIZE) {
                final Connection connection = DriverManager
                        .getConnection(DB_URL, DB_USER, DB_PASSWORD);
                final ProxyConnection proxyConnection = new ProxyConnection(connection, this);
                availableConnections.add(proxyConnection);
                return true;
            }
        } catch (SQLException e) {
            LOG.error("Error during creating connection", e);
            //todo: throw your exception
        } finally {
            writeLock.unlock();
        }
        return false;
    }

    //todo !! throw your own exeptions
    @Override
    public Connection takeConnection() throws InterruptedException {
        //todo: ? if waiting timeout expired
        // or is too long
        // -> check connections
        // or -> increase pool size
        writeLock.lock();
        try {
            while (availableConnections.isEmpty()) {
                hasAvailableConnections.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            usedConnections.add(connection);
            return connection;
        } finally {
            writeLock.unlock();
        }
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public boolean releaseConnection(Connection connection) {
        writeLock.lock();
        try {
            if (usedConnections.remove(connection)) {
                availableConnections.add((ProxyConnection) connection);
                hasAvailableConnections.signalAll();
                return true;
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean shutDown() {
        if (initialized) {
            closeConnections();
            deregisterDrivers();
            initialized = false;
            return true;
        }
        return false;
    }

    private void closeConnections() {
            closeConnections(this.availableConnections);
            closeConnections(this.usedConnections);
    }

    private void closeConnections(Collection<ProxyConnection> collection) {
        writeLock.lock();
        try {
            collection.forEach(this::closeConnection);
            collection.clear();
        } finally {
            writeLock.unlock();
        }
    }

    private void closeConnection(ProxyConnection conn) {
        try {
            conn.realClose();
            LOG.info("Connection {} closed", conn);
        } catch (SQLException e) {
            LOG.error("Could not close connection", e);
        }
    }

    //todo ? why static
    private static void deregisterDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOG.error("could not deregister driver", e);
            }
        }
    }
}
