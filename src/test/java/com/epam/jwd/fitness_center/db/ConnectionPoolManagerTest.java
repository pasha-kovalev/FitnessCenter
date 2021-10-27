package com.epam.jwd.fitness_center.db;


import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionPoolManagerTest {
    public static final int TIMEOUT = 1;
    ConnectionPoolManager cp = ConnectionPoolManager.getInstance();

    {
        cp.init();
    }

    @Test
    public void takeConnection_shouldReturnValidConnection_always() throws Exception{
        assertTrue(cp.takeConnection().isValid(TIMEOUT));
    }

    @Test
    public void isInitialized_shouldReturnTrue_afterInit() {
        assertTrue(cp.isInitialized());
    }

    @Test
    public void getNumOfConnections_shouldReturnInitialPoolSize_afterInit() {
        assertEquals(ConnectionPoolManager.INITIAL_POOL_SIZE, cp.getNumOfConnections());
    }

    @Test
    public void init_shouldReturnFalse_whenConnectionAlreadyInitialized() {
        assertFalse(cp.init());
    }

    @Test
    public void releaseConnection_shouldReturnTrue_whenUsedConnectionsNotEmpty() throws InterruptedException {
        Connection conn = cp.takeConnection();
        assertTrue(cp.releaseConnection(conn));
    }

    @Test
    public void releaseConnection_shouldReturnFalse_whenConnectionNotInUsedConnections() throws InterruptedException {
        Connection conn = cp.takeConnection();
        cp.releaseConnection(conn);
        assertFalse(cp.releaseConnection(conn));
    }

}