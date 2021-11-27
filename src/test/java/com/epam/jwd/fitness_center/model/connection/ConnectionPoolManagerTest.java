package com.epam.jwd.fitness_center.model.connection;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.epam.jwd.fitness_center.model.service.impl.UserServiceImpl;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.Optional;

import static at.favre.lib.crypto.bcrypt.BCrypt.MIN_COST;
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
    public void releaseConnection_shouldReturnTrue_whenUsedConnectionsNotEmpty() throws DatabaseConnectionException {
        Connection conn = cp.takeConnection();
        assertTrue(cp.releaseConnection(conn));
    }


    @Test
    public void deletemeplease() throws DatabaseConnectionException, ServiceException {
        final BCrypt.Hasher hasher = BCrypt.withDefaults();
        final BCrypt.Verifyer verifier = BCrypt.verifyer();
        Optional<User> user = ServiceProvider.getInstance().getUserService().findUserById(4L);

        String password_db = user.get().getPassword();
        String password_entered = "kova";

        final byte[] enteredPassword = password_entered.getBytes(StandardCharsets.UTF_8);
        final String passStr = hasher.hashToString(MIN_COST,password_db.toCharArray());
        final byte[] hashedPassword = passStr.getBytes(StandardCharsets.UTF_8);

        System.out.println((verifier.verify(enteredPassword,
                "$2a$04$iat8wqHwg8ciM7mH0fMae.m3XplXA3okdJFw1mEtFM1gN3Qw1O3iO".getBytes(StandardCharsets.UTF_8))
                .verified));
        System.out.println((verifier.verify(enteredPassword,
                password_db.getBytes(StandardCharsets.UTF_8))
                .verified));
    }
}