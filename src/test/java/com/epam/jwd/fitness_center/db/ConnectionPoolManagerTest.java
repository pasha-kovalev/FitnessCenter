package com.epam.jwd.fitness_center.db;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionPoolManagerTest {
    //todo use mocks
    public static final int TIMEOUT = 1;
    public static final double INCREASE_COEFF = 0.75;
    public static final int MIN_POOL_SIZE = 4;
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
        while (!(cp.getUsedConnectionsSize() >= INCREASE_COEFF * sizeBefore)) {
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
    public void releaseConnection_shouldReturnTrue_whenUsedConnectionsNotEmpty()  {
        Connection conn = cp.takeConnection();
        assertTrue(cp.releaseConnection(conn));
    }

    @Test
    public void releaseConnection_shouldReturnFalse_whenConnectionNotInUsedConnections()  {
        Connection conn = cp.takeConnection();
        cp.releaseConnection(conn);
        assertFalse(cp.releaseConnection(conn));
    }

    @Test
    public void cleanPoolThread_run_shouldCleanPool_whenConnectionLastUseTimeMoreThanDowntime() throws Exception {
        Thread.sleep(3000);
        int expected = Math.max(cp.getUsedConnectionsSize(), MIN_POOL_SIZE);
        assertEquals(expected, cp.getCurrentSize());
        cp.takeConnection();
        cp.takeConnection();
        cp.takeConnection();
        Thread.sleep(4000);
        int expected2 = Math.max(cp.getUsedConnectionsSize(), MIN_POOL_SIZE);
        assertEquals(expected2, cp.getCurrentSize());


    }

}