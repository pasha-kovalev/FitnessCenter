package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.CardDao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CardDaoImpl implements CardDao {
    public static final String EVENT_QUERY_NAME_PART_CREDIT_ORDER = "credit_order_";
    public static final String EVENT_QUERY_NAME_PART_DURATION = "duration_";
    private static final String FIND_CARD_BY_NUMBER = "SELECT id FROM card\n" +
            "WHERE number = ?";
    private static final String UPDATE_WITHDRAW = "UPDATE card\n" +
            "SET balance = balance - ?\n" +
            "WHERE number = ?";
    private static final String FIND_CARD_BY_AVAILABLE_BALANCE = "SELECT id FROM card\n" +
            "WHERE number = ? AND balance >= ?";
    private final ConnectionPool pool;

    CardDaoImpl(ConnectionPool pool) {
        this.pool = pool;
    }

    private static String createCreditCardEventQuery(String eventName) {
        return "CREATE EVENT " + eventName + "\n" +
                "    ON SCHEDULE EVERY 1 MONTH\n" +
                "        STARTS CURRENT_TIMESTAMP\n" +
                "        ENDS CURRENT_TIMESTAMP + INTERVAL (? - 1) MONTH\n" +
                "    DO\n" +
                "        BEGIN\n" +
                "            IF EXISTS(SELECT id FROM card WHERE number = ? AND balance >= ?) THEN\n" +
                "                UPDATE card SET balance = balance - ? WHERE number = ?;\n" +
                "            ELSE\n" +
                "                UPDATE `order`\n" +
                "                SET order_status_id = (SELECT id FROM order_status WHERE order_status='payment_arrears')\n" +
                "                WHERE `order`.id = ?\n" +
                "                      AND order_status_id = (SELECT id FROM order_status WHERE order_status='active');\n" +
                "            END IF;\n" +
                "        END";
    }

    @Override
    public boolean isCardExist(String cardNumber) throws DaoException {
        boolean isExist = false;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_CARD_BY_NUMBER)) {
            statement.setString(1, cardNumber);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isExist = true;
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException("could not execute select", e);
        }
        return isExist;
    }

    @Override
    public boolean withdraw(String cardNumber, BigDecimal amount) throws DaoException {
        boolean isSuccess = false;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_WITHDRAW)) {
            statement.setBigDecimal(1, amount);
            statement.setString(2, cardNumber);
            if (statement.executeUpdate() > 0) {
                isSuccess = true;
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException("could not execute update withdraw", e);
        }
        return isSuccess;
    }

    @Override
    public boolean isEnoughMoney(String cardNumber, BigDecimal amount) throws DaoException {
        boolean isEnough = false;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_CARD_BY_AVAILABLE_BALANCE)) {
            statement.setString(1, cardNumber);
            statement.setBigDecimal(2, amount);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isEnough = true;
            }
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException("could not execute select", e);
        }
        return isEnough;
    }

    @Override
    public void establishCredit(String cardNumber, BigDecimal amountPerMonth, int numberOfMonths,
                                long orderId, long userId) throws DaoException {
        String sqlEventName = EVENT_QUERY_NAME_PART_CREDIT_ORDER + orderId +
                EVENT_QUERY_NAME_PART_DURATION + numberOfMonths;
        try (Connection connection = pool.takeConnection();
             PreparedStatement statement = connection.prepareStatement(createCreditCardEventQuery(sqlEventName))) {
            statement.setInt(1, numberOfMonths);
            statement.setString(2, cardNumber);
            statement.setBigDecimal(3, amountPerMonth);
            statement.setBigDecimal(4, amountPerMonth);
            statement.setString(5, cardNumber);
            statement.setLong(6, orderId);
            statement.execute();
        } catch (DatabaseConnectionException | SQLException e) {
            throw new DaoException("could not execute credit query", e);
        }
    }
}
