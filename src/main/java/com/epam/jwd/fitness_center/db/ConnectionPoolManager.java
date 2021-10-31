package com.epam.jwd.fitness_center.db;

import java.io.FileNotFoundException;
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
import java.util.concurrent.locks.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolManager implements ConnectionPool {
    private static final Logger LOG = LogManager.getLogger(ConnectionPoolManager.class);

    public static final int INITIAL_POOL_SIZE = 5;
    public static final int MIN_POOL_SIZE = 4;
    public static final int MAX_POOL_SIZE = 8;
    public static final double INCREASE_COEFF = 0.75;

    //todo ? should be encrypted
    private static final String CONFIG_PATH = "/config.properties";
    private static final String DB_URL;
    private static final String DB_USER;
    private static final String DB_PASSWORD;

    private static Properties props;

    private final Queue<ProxyConnection> availableConnections = new ArrayDeque<>();
    private final List<ProxyConnection> usedConnections = new ArrayList<>();

    private final ReentrantLock lock = new ReentrantLock(true);
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock(true);
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    private final Condition hasAvailableConnections = writeLock.newCondition();
    private final Condition needToCreateConnections = lock.newCondition();

    //In seconds
    private long cleaningInterval = 60;
    private long maxConnectionDownTime = 60;

    private boolean initialized = false;
    private boolean isShutDown = false;
    private IncreasePoolThread increasePoolThread;
    private CleanPoolThread cleanPoolThread;

    static {
        InputStream is = null;
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
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.warn("Unable to close InputStream in db connection pool", e);
                }
            }
        }

        DB_URL = props.getProperty("db.host");
        DB_USER = props.getProperty("db.login");
        DB_PASSWORD = props.getProperty("db.password");
    }

    //todo: read
    // about
    // threading, exception
    // in
    // Effective
    // Java


    private ConnectionPoolManager() {
    }

    private static class ConnectionPoolManagerHolder {
        private static final ConnectionPoolManager instance = new ConnectionPoolManager();
    }

    public static ConnectionPoolManager getInstance() {
        return ConnectionPoolManagerHolder.instance;
    }

    public long getCleaningInterval() {
        return cleaningInterval;
    }

    public void setCleaningInterval(long cleaningInterval) {
        if(cleaningInterval < 0) {
            //todo ? need to throw exception
            LOG.warn("Trying to assign a negative value to DB cleanInterval: {}", cleaningInterval);
            return;
        }
        this.cleaningInterval = cleaningInterval;
    }

    public long getMaxConnectionDownTime() {
        return maxConnectionDownTime;
    }

    public void setMaxConnectionDownTime(long maxConnectionDownTime) {
        if(maxConnectionDownTime < 0) {
            //todo ? need to throw exception
            LOG.warn("Trying to assign a negative value to maxConnectionDownTime: {}", maxConnectionDownTime);
            return;
        }
        this.maxConnectionDownTime = maxConnectionDownTime;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
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

    public int getAvailableConnectionsSize() {
        readLock.lock();
        try {
            return availableConnections.size();
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean init() {
        lock.lock();
        try {
            if (isShutDown) {
                LOG.error("Not able to init connections in pool because of pool is shut down");
            }
            else if (!initialized) {
                increasePoolThread = new IncreasePoolThread();
                cleanPoolThread = new CleanPoolThread();
                increasePoolThread.start();
                cleanPoolThread.start();
                initializeConnections();
                initialized = true;
                return true;
            }
        } finally {
            lock.unlock();
        }
        return false;
    }

    //todo: ? why NMikle in initializeConnections(int amount, boolean failOnConnectionException)
    // use boolean parameter
    private void initializeConnections() {
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            addConnection();
        }
    }

    private boolean addConnection() {
        if (getCurrentSize() < MAX_POOL_SIZE) {
            Optional<ProxyConnection> optionalProxyConnection = createProxyConnection();
            if(optionalProxyConnection.isPresent()) {
                writeLock.lock();
                try {
                    availableConnections.add(optionalProxyConnection.get());
                    hasAvailableConnections.signalAll();
                    LOG.trace("added a new connection: {}", optionalProxyConnection.get());
                    return true;
                } finally {
                    writeLock.unlock();
                }
            }
        }
        return false;

    }

    private boolean addConnection(ProxyConnection proxyConnection) {
        if (getCurrentSize() < MAX_POOL_SIZE) {
            writeLock.lock();
            try {
                availableConnections.add(proxyConnection);
                hasAvailableConnections.signalAll();
                LOG.trace("added existing connection: {}", proxyConnection);
                return true;
            } finally {
                writeLock.unlock();
            }
        }
        return false;
    }

    private void addConnections(List<ProxyConnection> proxyConnections) {
        proxyConnections.forEach(this::addConnection);
    }

    private Optional<ProxyConnection> createProxyConnection() {
        try {
            final Connection connection = DriverManager
                    .getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return Optional.of(new ProxyConnection(connection, this));
        } catch (SQLException e) {
            //todo ? need to throw exception
            LOG.error("Unable to create connection", e);
        }
        return Optional.empty();
    }

    @Override
    public Connection takeConnection() throws InterruptedException {
       writeLock.lock();
        try {
            increasePoolThread.checkIncreaseCondition();
            while (availableConnections.isEmpty()) {
                //todo ? if for a long time
                hasAvailableConnections.await();
            }
            final ProxyConnection connection = availableConnections.poll();
            usedConnections.add(connection);
            LOG.trace("Connection taken: {}", connection);
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
                ((ProxyConnection) connection).setLastTakeDate(LocalDateTime.now());
                hasAvailableConnections.signalAll();
                LOG.trace("Connection released: {}", connection);
                return true;
            }
            return false;
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public boolean shutDown() {
        if (initialized && !isShutDown) {
            LOG.trace("Connection Pool is closing...");
            isShutDown = true;
            closeAllConnections();
            deregisterDrivers();
            initialized = false;
            return true;
        }
        return false;
    }

    private void closeAllConnections() {
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
            //todo ? need to throw exception
            LOG.error("Could not close connection", e);
        }
    }

    private static void deregisterDrivers() {
        final Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(drivers.nextElement());
            } catch (SQLException e) {
                //todo ? need to throw exception
                LOG.error("could not deregister driver", e);
            }
        }
    }

    class IncreasePoolThread extends Thread {
        private boolean isToIncrease = false;

        public IncreasePoolThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(!isShutDown) {
                lock.lock();
                try {
                    while (!isToIncrease) {
                        needToCreateConnections.await();
                    }

                    if(isShutDown) break;

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
            LOG.trace("In Increase()");
            List<ProxyConnection> proxyConnections = new ArrayList<>();
            Optional<ProxyConnection> optionalProxyConnection1 = createProxyConnection();
            Optional<ProxyConnection> optionalProxyConnection2 = createProxyConnection();
            optionalProxyConnection1.ifPresent(proxyConnections::add);
            optionalProxyConnection2.ifPresent(proxyConnections::add);
            addConnections(proxyConnections);
            isToIncrease = false;
        }

        private void checkIncreaseCondition() {
            if(getUsedConnectionsSize() >= INCREASE_COEFF * ConnectionPoolManager.this.getCurrentSize()) {
                lock.lock();
                try {
                    isToIncrease = true;
                    needToCreateConnections.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    class CleanPoolThread extends Thread {
        public CleanPoolThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while(!isShutDown) {
                try {
                    TimeUnit.SECONDS.sleep(cleaningInterval);
                } catch (InterruptedException e) {
                    LOG.warn("CleanPoolThread interrupted", e);
                    Thread.currentThread().interrupt();
                }

                if(isShutDown) break;

                cleanByDownTime();
            }
        }

        //todo ? make a copy  of availableConnections
        private void cleanByDownTime() {
            if (!isShutDown && getCurrentSize() > ConnectionPoolManager.MIN_POOL_SIZE) {
                readLock.lock();
                try {
                    for (ProxyConnection conn : availableConnections) {
                        long timePassed = Duration.between(conn.getLastTakeDate(), LocalDateTime.now()).getSeconds();
                        if (getCurrentSize() == ConnectionPoolManager.MIN_POOL_SIZE) {
                            break;
                        }
                        if(timePassed > maxConnectionDownTime) {
                            deleteConnection(conn);
                        }
                    }
                } finally {
                    readLock.unlock();
                }
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
