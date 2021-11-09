package com.epam.jwd.fitness_center.db;

import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConnectionPoolManagerTest {
    public static final int TIMEOUT = 1;
    public static final double INCREASE_COEFF = 0.75;
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
    public void takeConnection_shouldReturnValidConnection_always() throws Exception{
        assertTrue(cp.takeConnection().isValid(TIMEOUT));
    }

    //fixme loops are undesirable in tests
    @Test
    public void takeConnection_shouldCallMethodThatCreateNewConnections_whenUsedConditionAmountMoreThanEstablished()
            throws Exception {
        int sizeBefore = cp.getCurrentSize();
        while (!(cp.getUsedConnectionsSize() >= INCREASE_COEFF * sizeBefore)) {
            cp.takeConnection();
        }
        cp.takeConnection();
        Thread.sleep(1000);
        int sizeAfter = cp.getCurrentSize();
        assertTrue(sizeBefore < sizeAfter);
    }

    @Test
    public void takeConnection_shouldWaitUntilNewConnectionsAppear_whenAvailableConnectionsIsEmpty()
            throws Exception {
        int sizeBefore = cp.getCurrentSize() - cp.getUsedConnectionsSize();
        while (!(cp.getUsedConnectionsSize() == cp.getCurrentSize())) {
            cp.takeConnection();
        }
        assertFalse(cp.getCurrentSize() - cp.getUsedConnectionsSize() > 0);
        Thread.sleep(1000);
        assertTrue(cp.getCurrentSize() - cp.getUsedConnectionsSize() > 0);
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
}