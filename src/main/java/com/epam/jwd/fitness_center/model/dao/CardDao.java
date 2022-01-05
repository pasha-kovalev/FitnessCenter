package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;

import java.math.BigDecimal;

public interface CardDao {
    boolean isCardExist(String cardNumber) throws DaoException;

    boolean withdraw(String cardNumber, BigDecimal money) throws DaoException;

    boolean isEnoughMoney(String cardNumber, BigDecimal amount) throws DaoException;

    void establishCredit(String cardNumber, BigDecimal amountPerMonth, int numberOfMonths, long orderId, long userId)
            throws DaoException;
}