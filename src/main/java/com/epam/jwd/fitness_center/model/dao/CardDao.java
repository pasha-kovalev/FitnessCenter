package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;

import java.math.BigDecimal;

/**
 * The interface represents bank card dao
 */
public interface CardDao {
    /**
     * Check is given card number exists in db
     *
     * @param cardNumber card number
     * @return true if exists
     * @throws DaoException when error while query execution occurs
     */
    boolean isCardExist(String cardNumber) throws DaoException;

    /**
     * Withdraws given amount of money from card
     *
     * @param cardNumber card number
     * @param money      amount of money
     * @return true if successful
     * @throws DaoException when error while query execution occurs
     */
    boolean withdraw(String cardNumber, BigDecimal money) throws DaoException;

    /**
     * Check is given card has enough money
     *
     * @param cardNumber card number
     * @param amount     amount of money
     * @return true if enough money
     * @throws DaoException when error while query execution occurs
     */
    boolean isEnoughMoney(String cardNumber, BigDecimal amount) throws DaoException;

    /**
     * Establishes a credit
     *
     * @param cardNumber     card number
     * @param amountPerMonth amount of money to withdraw per month
     * @param numberOfMonths credit duration in months
     * @param orderId        id of order
     * @param userId         id of user
     * @throws DaoException when error while query execution occurs
     */
    void establishCredit(String cardNumber, BigDecimal amountPerMonth, int numberOfMonths, long orderId, long userId)
            throws DaoException;
}