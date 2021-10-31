package com.epam.jwd.fitness_center.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionPoolManagerTest {
    //todo use mocks
    public static final int TIMEOUT = 1;

    ConnectionPoolManager cp = ConnectionPoolManager.getInstance();

    {
        cp.setCleaningInterval(3);
        cp.setMaxConnectionDownTime(2);
        cp.init();
    }

    @AfterAll
    public void teardown() {
        cp.shutDown();
    }

    @Test
    public void takeConnection_shouldReturnValidConnection_always() throws Exception{
        assertTrue(cp.takeConnection().isValid(TIMEOUT));
    }

    @Test
    public void takeConnection_shouldSignalNeedToCreateConnectionsCondition_whenUsedConditionAmountMoreThanEstablished()
            throws Exception {
        int sizeBefore = cp.getCurrentSize();
        while (!(cp.getUsedConnectionsSize() >= ConnectionPoolManager.INCREASE_COEFF * sizeBefore)) {
            cp.takeConnection();
        }
        cp.takeConnection();
        //todo: use mocks
        Thread.sleep(3000);
        int sizeAfter = cp.getCurrentSize();
        assertTrue(sizeBefore < sizeAfter);
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
    public void releaseConnection_shouldReturnTrue_whenUsedConnectionsNotEmpty() throws Exception {
        Connection conn = cp.takeConnection();
        assertTrue(cp.releaseConnection(conn));
    }

    @Test
    public void releaseConnection_shouldReturnFalse_whenConnectionNotInUsedConnections() throws Exception {
        Connection conn = cp.takeConnection();
        cp.releaseConnection(conn);
        assertFalse(cp.releaseConnection(conn));
    }

    @Test
    public void cleanPoolThread_run_shouldCleanPool_whenConnectionLastUseTimeMoreThanDowntime() throws Exception {
        //todo use mocks
        Thread.sleep(6000);
        int expected = Math.max(cp.getUsedConnectionsSize(), ConnectionPoolManager.MIN_POOL_SIZE);
        assertEquals(expected, cp.getCurrentSize());
    }

}