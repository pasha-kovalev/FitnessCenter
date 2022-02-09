package com.epam.jwd.fitness_center.model.connection;

import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.lang.reflect.Field;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionPoolManagerTest {
    public static final int TIMEOUT = 1;
    ConnectionPoolManager cp = ConnectionPoolManager.getInstance();

    @BeforeAll
    public void init() throws Exception {
        Field ci = ConnectionPoolManager.class.getDeclaredField("cleaningInterval");
        Field mcd = ConnectionPoolManager.class.getDeclaredField("maxConnectionDowntime");
        ci.setAccessible(true);
        mcd.setAccessible(true);
        ci.setInt(cp, 3);
        mcd.setInt(cp, 2);
        cp.init();
    }

    @AfterAll
    public void teardown() throws InterruptedException {
        Thread.sleep(4000);
        cp.shutDown();
    }

    @Test
    public void takeConnection_shouldReturnValidConnection_always() throws Exception {
        assertTrue(cp.takeConnection().isValid(TIMEOUT));
    }

    @Test
    public void isInitialized_shouldReturnTrue_afterInit() {
        assertTrue(cp.isInitialized());
    }

    @Test
    public void init_shouldReturnFalse_whenConnectionAlreadyInitialized() {
        assertFalse(cp.init());
    }

    @Test
    public void releaseConnection_shouldReturnTrue_whenUsedConnectionsNotEmpty() throws DatabaseConnectionException {
        Connection conn = cp.takeConnection();
        assertTrue(cp.releaseConnection(conn));
    }
}