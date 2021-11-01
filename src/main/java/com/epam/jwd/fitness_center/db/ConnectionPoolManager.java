package com.epam.jwd.fitness_center.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.*;

import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolManager implements ConnectionPool {
    private static final Logger LOG = LogManager.getLogger(ConnectionPoolManager.class);

    private static ConnectionPoolManager instance;
    private static AtomicBoolean hasInstance = new AtomicBoolean(false);

    private static final String POOL_CONFIG_PATH = "database/pool.properties";
    private static final int DEFAULT_POOL_SIZE = 8;
    private static final int DEFAULT_MIN_POOL_SIZE = 4;
    private static final int DEFAULT_MAX_POOL_SIZE = 15;
    private static final double DEFAULT_INCREASE_COEFF = 0.75;

    //In seconds
    private static final int DEFAULT_CLEANING_INTERVAL = 60;
    private static final int DEFAULT_MAX_CONNECTION_DOWNTIME = 60;

    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> usedConnections = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock(true);
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    private final Condition hasAvailableConnections = writeLock.newCondition();
    private final Condition needToCreateConnections = lock.newCondition();

    private int poolSize;
    private int minPoolSize;
    private int maxPoolSize;
    private double increaseCoeff;
    private int cleaningInterval;
    private int maxConnectionDowntime;

    private AtomicBoolean isInitialized = new AtomicBoolean(false);
    private AtomicBoolean isShutDown = new AtomicBoolean(false);

    private IncreasePoolThread increasePoolThread;

    private ConnectionPoolManager() {
        try(InputStream is = ConnectionPool.class.getClassLoader().getResourceAsStream(POOL_CONFIG_PATH)) {
            if(is == null) {
                LOG.warn("Unable to find connection pool property file");
                assignDefault();
            }
            Properties poolProperties =  new Properties();
            poolProperties.load(is);
            poolSize = Integer.parseInt(poolProperties.getProperty("size"));
            minPoolSize = Integer.parseInt(poolProperties.getProperty("minSize"));
            maxPoolSize = Integer.parseInt(poolProperties.getProperty("maxSize"));
            increaseCoeff = Double.parseDouble(poolProperties.getProperty("increaseCoefficient"));
            cleaningInterval = Integer.parseInt(poolProperties.getProperty("cleanInterval"));
            maxConnectionDowntime = Integer.parseInt(poolProperties.getProperty("maxConnectionDowntime"));
            LOG.info("Connection pool property file loaded");
        } catch (IOException e) {
            LOG.warn("Unable to open connection pool property file", e);
            assignDefault();
        } catch (NumberFormatException e) {
            LOG.warn("Incorrect connection pool property file. Unable to parse value", e);
            assignDefault();
        }

        if (minPoolSize < 0 || minPoolSize > maxPoolSize || poolSize > maxPoolSize || poolSize < minPoolSize
                || cleaningInterval < 0 || maxConnectionDowntime < 0 || increaseCoeff < 0 || increaseCoeff > 1) {
            LOG.warn("Incorrect values of properties in connection pool property file");
            assignDefault();
        }
    }

    public static ConnectionPoolManager getInstance() {
        while (instance == null) {
            if(hasInstance.compareAndSet(false, true)) {
                instance = new ConnectionPoolManager();
            }
        }
        return instance;
    }

    @Override
    public boolean isInitialized() {
        return isInitialized.get();
    }

    public int getCurrentSize() {
        readLock.lock();
        try {
            return availableConnections.size() + usedConnections.size();
        } finally {
            readLock.unlock();
        }
    }

    public int getUsedConnectionsSize() {
        readLock.lock();
        try {
            return usedConnections.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (isShutDown.get()) {
                LOG.error("Unable to init connections in pool because of pool is shut down");
            }
            else if (!isInitialized.get()) {
                increasePoolThread = new IncreasePoolThread();
                increasePoolThread.start();
                new CleanPoolThread().start();
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

    @Override
    public Connection takeConnection() {
       writeLock.lock();
        try {
            increasePoolThread.checkIncreaseCondition();
            while (availableConnections.isEmpty()) {
                try {
                    hasAvailableConnections.await();
                } catch (InterruptedException e) {
                    LOG.warn("Interrupted!", e);
                    Thread.currentThread().interrupt();
                }
            }
            final ProxyConnection connection = availableConnections.poll();
            usedConnections.add(connection);
            LOG.trace("Connection taken: {}", connection);
            return connection;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean releaseConnection(Connection connection) {
        if(connection instanceof ProxyConnection) {
            writeLock.lock();
            try {
                if (usedConnections.remove(connection)) {
                    availableConnections.add((ProxyConnection) connection);
                    ((ProxyConnection) connection).setLastTakeDate(LocalDateTime.now());
                    hasAvailableConnections.signalAll();
                    LOG.trace("Connection released: {}", connection);
                    return true;
                }
            } finally {
                writeLock.unlock();
            }
        }
        return false;
    }

    @Override
    public boolean shutDown() {
        if (isInitialized.get() && !isShutDown.get()) {
            LOG.info("Connection Pool is closing...");
            isShutDown.set(true);
            closeConnections();
            deregisterDrivers();
            isInitialized.set(false);
            LOG.info("Connection Pool is closed");
            return true;
        }
        return false;
    }

    private void assignDefault() {
        poolSize = DEFAULT_POOL_SIZE;
        minPoolSize = DEFAULT_MIN_POOL_SIZE;
        maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        increaseCoeff = DEFAULT_INCREASE_COEFF;
        cleaningInterval = DEFAULT_CLEANING_INTERVAL;
        maxConnectionDowntime = DEFAULT_MAX_CONNECTION_DOWNTIME;
    }

    private void addConnection() throws DatabaseConnectionException {
        writeLock.lock();
        try {
            if (getCurrentSize() < maxPoolSize) {
                ProxyConnection proxyConnection = ConnectionFactory.createProxyConnection();
                availableConnections.add(proxyConnection);
                hasAvailableConnections.signalAll();
                LOG.trace("added a new connection: {}", proxyConnection);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void addConnection(ProxyConnection proxyConnection) {
        writeLock.lock();
        try {
            if(getCurrentSize() < maxPoolSize) {
                availableConnections.add(proxyConnection);
                hasAvailableConnections.signalAll();
                LOG.trace("added existing connection: {}", proxyConnection);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void closeConnections() {
            closeConnections(availableConnections);
            closeConnections(usedConnections);
    }

    private void closeConnections(Collection<ProxyConnection> collection) {
        writeLock.lock();
        try {
            for (ProxyConnection proxyConnection : collection) {
                proxyConnection.realClose();
                LOG.trace("Connection {} closed", proxyConnection);
            }
            collection.clear();
        } catch (SQLException e) {
            LOG.error("Unable to close connection", e);
        }
        finally {
            writeLock.unlock();
        }
    }

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

    private class IncreasePoolThread extends Thread {
        private boolean isToIncrease = false;

        IncreasePoolThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(!isShutDown.get()) {
                lock.lock();
                try {
                    while (!isToIncrease) {
                        needToCreateConnections.await();
                    }

                    if(isShutDown.get()) break;
                    increase();
                } catch (InterruptedException e) {
                    LOG.warn("CleanPoolThread interrupted", e);
                    Thread.currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }

        private void increase() {
            LOG.trace("In increase()");
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

        private void checkIncreaseCondition() {
            lock.lock();
            try {
                if(getUsedConnectionsSize() >= increaseCoeff * ConnectionPoolManager.this.getCurrentSize()) {
                    isToIncrease = true;
                    needToCreateConnections.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }

        private void addConnections(List<ProxyConnection> proxyConnections) {
            proxyConnections.forEach(ConnectionPoolManager.this::addConnection);
        }
    }

    private class CleanPoolThread extends Thread {
        CleanPoolThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(!isShutDown.get()) {
                try {
                    TimeUnit.SECONDS.sleep(cleaningInterval);
                } catch (InterruptedException e) {
                    LOG.warn("CleanPoolThread interrupted", e);
                    Thread.currentThread().interrupt();
                }

                if(isShutDown.get()) break;
                cleanByDownTime();
            }
        }

        private void cleanByDownTime() {
            readLock.lock();
            try {
                if (!isShutDown.get() && getCurrentSize() > minPoolSize) {
                    for (ProxyConnection conn : availableConnections) {
                        long timePassed = Duration.between(conn.getLastTakeDate(), LocalDateTime.now()).getSeconds();
                        if (getCurrentSize() == minPoolSize) {
                            break;
                        }
                        if (timePassed > maxConnectionDowntime) {
                            deleteConnection(conn);
                        }
                    }
                }
            } finally {
                readLock.unlock();
            }
        }

        private void deleteConnection(ProxyConnection conn) {
            readLock.unlock();
            writeLock.lock();
            try {
                if(availableConnections.remove(conn)) {
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
