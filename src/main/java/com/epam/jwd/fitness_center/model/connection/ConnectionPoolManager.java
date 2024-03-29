package com.epam.jwd.fitness_center.model.connection;

import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Represents connection pool
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public final class ConnectionPoolManager implements ConnectionPool {
    private static final Logger LOG = LogManager.getLogger(ConnectionPoolManager.class);

    private static final String POOL_SIZE_PROPERTY = "size";
    private static final String POOL_MIN_SIZE_PROPERTY = "minSize";
    private static final String POOL_MAX_SIZE_PROPERTY = "maxSize";
    private static final String POOL_INCREASE_COEFFICIENT_PROPERTY = "increaseCoefficient";
    private static final String POOL_CLEAN_INTERVAL_PROPERTY = "cleanInterval";
    private static final String POOL_CONFIG_PATH = "properties/database/pool.properties";

    private static final int DEFAULT_POOL_SIZE = 8;
    private static final int DEFAULT_MIN_POOL_SIZE = 4;
    private static final int DEFAULT_MAX_POOL_SIZE = 15;
    private static final double DEFAULT_INCREASE_COEFF = 0.75;
    private static final int DEFAULT_CLEANING_INTERVAL = 60;
    private static final int DEFAULT_MAX_CONNECTION_DOWNTIME = 60;

    private static final AtomicBoolean hasInstance = new AtomicBoolean(false);
    private static ConnectionPoolManager instance;

    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> usedConnections = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock(true);
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();
    private final Condition hasAvailableConnections = writeLock.newCondition();

    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private final AtomicBoolean isShutDown = new AtomicBoolean(false);

    private int poolSize;
    private int minPoolSize;
    private int maxPoolSize;
    private double increaseCoeff;
    private int cleaningInterval;
    private int maxConnectionDowntime;

    private Timer cleanTimer;
    private IncreasePoolThread increasePoolThread;

    private ConnectionPoolManager() {
        try (InputStream is = ConnectionPool.class.getClassLoader().getResourceAsStream(POOL_CONFIG_PATH)) {
            if (is == null) {
                LOG.error("Unable to find connection pool property file");
                assignDefault();
            }
            Properties poolProperties = new Properties();
            poolProperties.load(is);
            poolSize = Integer.parseInt(poolProperties.getProperty(POOL_SIZE_PROPERTY));
            minPoolSize = Integer.parseInt(poolProperties.getProperty(POOL_MIN_SIZE_PROPERTY));
            maxPoolSize = Integer.parseInt(poolProperties.getProperty(POOL_MAX_SIZE_PROPERTY));
            increaseCoeff = Double.parseDouble(poolProperties.getProperty(POOL_INCREASE_COEFFICIENT_PROPERTY));
            cleaningInterval = Integer.parseInt(poolProperties.getProperty(POOL_CLEAN_INTERVAL_PROPERTY));
            maxConnectionDowntime = Integer.parseInt(poolProperties.getProperty("maxConnectionDowntime"));
            LOG.info("Connection pool property file loaded");
        } catch (IOException e) {
            LOG.error("Unable to open connection pool property file", e);
            assignDefault();
        } catch (NumberFormatException e) {
            LOG.error("Incorrect connection pool property file. Unable to parse value", e);
            assignDefault();
        }
        if (minPoolSize < 0 || minPoolSize > maxPoolSize || poolSize > maxPoolSize || poolSize < minPoolSize
                || cleaningInterval < 0 || maxConnectionDowntime < 0 || increaseCoeff < 0 || increaseCoeff > 1) {
            LOG.error("Incorrect values of properties in connection pool property file");
            assignDefault();
        }
    }

    /**
     * Gets the instance of connection pool manager
     *
     * @return connection pool manager
     */
    public static ConnectionPoolManager getInstance() {
        while (instance == null) {
            if (hasInstance.compareAndSet(false, true)) {
                instance = new ConnectionPoolManager();
            }
        }
        return instance;
    }

    /**
     * Deregister drivers
     */
    private static void deregisterDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                LOG.error("Unable to deregister driver", e);
            }
        }
    }

    /**
     * Gets current size of pool
     *
     * @return number of all connections in pool
     */
    public int getCurrentSize() {
        readLock.lock();
        try {
            return availableConnections.size() + usedConnections.size();
        } finally {
            readLock.unlock();
        }
    }

    /**
     * Gets number of used connections
     *
     * @return number of used connections
     */
    public int getUsedConnectionsSize() {
        readLock.lock();
        try {
            return usedConnections.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean isInitialized() {
        return isInitialized.get();
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (isShutDown.get()) {
                LOG.error("Unable to init connections in pool because of pool is shut down");
            } else if (!isInitialized.get()) {
                initDaemonThreads();
                for (int i = 0; i < poolSize; i++) {
                    addConnection();
                }
                isInitialized.set(true);
                return true;
            }
        } catch (DatabaseConnectionException e) {
            LOG.fatal("Unable to initialize connection pool", e);
            throw new RuntimeException("Unable to initialize connection pool");
        } finally {
            lock.unlock();
        }
        return false;
    }

    /**
     * Initializes daemon threads
     */
    private void initDaemonThreads() {
        cleanTimer = new Timer();
        cleanTimer.schedule(new CleanPoolThread(), 0, cleaningInterval * 1000L);
        increasePoolThread = new IncreasePoolThread();
        increasePoolThread.start();
    }

    @Override
    public Connection takeConnection() throws DatabaseConnectionException {
        writeLock.lock();
        if (!isInitialized.get()) init();
        try {
            checkCondition();
            final ProxyConnection connection = availableConnections.poll();
            usedConnections.add(connection);
            return connection;
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Checks is has available connection to take from pool or is need to increase pool size
     *
     * @throws DatabaseConnectionException if thread is interrupted
     */
    private void checkCondition() throws DatabaseConnectionException {
        increasePoolThread.checkIncreaseCondition();
        while (getUsedConnectionsSize() == maxPoolSize || availableConnections.isEmpty()) {
            try {
                hasAvailableConnections.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new DatabaseConnectionException("Thread is interrupted", e);
            }
        }
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        writeLock.lock();
        try {
            if (connection instanceof ProxyConnection && isInitialized.get()) {
                if (usedConnections.remove(connection)) {
                    availableConnections.add((ProxyConnection) connection);
                    ((ProxyConnection) connection).setLastTakeDate(LocalDateTime.now());
                    hasAvailableConnections.signalAll();
                    return true;
                }
            } else {
                LOG.error("Unable to release connection: {}, pool initialized: {}", connection,
                        isInitialized.get());
            }
        } finally {
            writeLock.unlock();
        }
        return false;
    }

    @Override
    public boolean shutDown() {
        if (isInitialized.get() && !isShutDown.get()) {
            LOG.info("Connection Pool is closing...");
            isShutDown.set(true);
            cleanTimer.cancel();
            cleanTimer.purge();
            increasePoolThread.shutDown();
            closeConnections();
            deregisterDrivers();
            isInitialized.set(false);
            LOG.info("Connection Pool is closed");
            return true;
        }
        return false;
    }

    /**
     * Assigns default values
     */
    private void assignDefault() {
        poolSize = DEFAULT_POOL_SIZE;
        minPoolSize = DEFAULT_MIN_POOL_SIZE;
        maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        increaseCoeff = DEFAULT_INCREASE_COEFF;
        cleaningInterval = DEFAULT_CLEANING_INTERVAL;
        maxConnectionDowntime = DEFAULT_MAX_CONNECTION_DOWNTIME;
    }

    /**
     * Adds new connection to pool
     *
     * @throws DatabaseConnectionException if error while creating connection occurred
     */
    private void addConnection() throws DatabaseConnectionException {
        writeLock.lock();
        try {
            if (getCurrentSize() < maxPoolSize) {
                ProxyConnection proxyConnection = ConnectionFactory.createProxyConnection();
                availableConnections.add(proxyConnection);
                hasAvailableConnections.signalAll();
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Adds existed proxy connection to pool
     *
     * @param proxyConnection proxy connection
     */
    private void addConnection(ProxyConnection proxyConnection) {
        writeLock.lock();
        try {
            if (getCurrentSize() < maxPoolSize) {
                if (proxyConnection == null) {
                    LOG.error("proxyConnection is null");
                    return;
                }
                availableConnections.add(proxyConnection);
                hasAvailableConnections.signalAll();
            } else {
                LOG.warn("Unable to add connection because pool is full");
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Closes all connections
     */
    private void closeConnections() {
        closeConnections(availableConnections);
        closeConnections(usedConnections);
    }

    /**
     * Closes all connections in given collection
     *
     * @param collection collection of connections
     */
    private void closeConnections(Collection<ProxyConnection> collection) {
        writeLock.lock();
        try {
            for (ProxyConnection proxyConnection : collection) {
                proxyConnection.realClose();
            }
            collection.clear();
        } catch (SQLException e) {
            LOG.error("Unable to close connection", e);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * Class for pool increase
     */
    private class IncreasePoolThread extends Thread {
        private final Condition needToCreateConnections = lock.newCondition();
        private boolean isToIncrease = false;

        IncreasePoolThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (!isShutDown.get()) {
                lock.lock();
                try {
                    while (!isToIncrease && !isShutDown.get()) {
                        needToCreateConnections.await();
                    }

                    if (isShutDown.get()) break;
                    increase();
                } catch (InterruptedException e) {
                    LOG.info("CleanPoolThread interrupted");
                } finally {
                    lock.unlock();
                }
            }
        }

        /**
         * Increases amount of pool available connections by two connections
         */
        private void increase() {
            List<ProxyConnection> proxyConnections = new ArrayList<>();
            try {
                ProxyConnection proxyConnection1 = ConnectionFactory.createProxyConnection();
                ProxyConnection proxyConnection2 = ConnectionFactory.createProxyConnection();
                proxyConnections.add(proxyConnection1);
                proxyConnections.add(proxyConnection2);
                addConnections(proxyConnections);
            } catch (DatabaseConnectionException e) {
                LOG.error("Unable to increase connection pool size", e);
            } finally {
                isToIncrease = false;
            }
        }

        /**
         * Checks if there are enough connections
         */
        private void checkIncreaseCondition() {
            if (!isToIncrease) {
                lock.lock();
                try {
                    if (getUsedConnectionsSize() >= increaseCoeff * ConnectionPoolManager.this.getCurrentSize()) {
                        isToIncrease = true;
                        needToCreateConnections.signalAll();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }

        /**
         * Increases amount of pool available connections by given connections in list
         *
         * @param proxyConnections list of connections
         */
        private void addConnections(List<ProxyConnection> proxyConnections) {
            proxyConnections.forEach(ConnectionPoolManager.this::addConnection);
        }

        /**
         * Interrupts thread
         */
        public void shutDown() {
            if (!isInterrupted()) {
                this.interrupt();
            }
        }
    }

    /**
     * Class for pool decrease
     */
    private class CleanPoolThread extends TimerTask {
        @Override
        public void run() {
            if (isShutDown.get()) return;
            cleanByDownTime();
        }

        /**
         * Checks downtime of connection and removes it if downtime is greater than the specified
         */
        private void cleanByDownTime() {
            readLock.lock();
            try {
                if (!isShutDown.get() && getCurrentSize() > minPoolSize) {
                    boolean needToClean = false;
                    List<ProxyConnection> connectionsToRemove = new ArrayList<>();
                    for (ProxyConnection conn : availableConnections) {
                        long timePassed = Duration.between(conn.getLastTakeDate(), LocalDateTime.now()).getSeconds();
                        if (getCurrentSize() == minPoolSize) {
                            break;
                        }
                        if (timePassed > maxConnectionDowntime) {
                            connectionsToRemove.add(conn);
                            needToClean = true;
                        }
                    }
                    if (needToClean) {
                        connectionsToRemove.forEach(this::deleteConnection);
                    }
                }
            } finally {
                readLock.unlock();
            }
        }

        /**
         * Removes given connection from pool
         *
         * @param conn proxy connection
         */
        private void deleteConnection(ProxyConnection conn) {
            readLock.unlock();
            writeLock.lock();
            try {
                if (availableConnections.remove(conn)) {
                    conn.realClose();
                }
            } catch (SQLException e) {
                LOG.error("Unable to close connection", e);
            } finally {
                writeLock.unlock();
                readLock.lock();
            }
        }
    }
}
