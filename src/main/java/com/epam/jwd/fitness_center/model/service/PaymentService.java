package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;

import java.math.BigDecimal;

/**
 * The interface represents payment service
 */
public interface PaymentService {
    BigDecimal DEFAULT_CREDIT_PERCENTAGE = BigDecimal.valueOf(5);
    int DEFAULT_CREDIT_PERIOD = 3;

    /**Establishes a credit
     * @param cardNumber card number
     * @param order order
     * @throws ServiceException while unable to perform a service action
     */
    void establishCredit(String cardNumber, Order order)
            throws ServiceException;

    /**Performs payment
     * @param cardNumber card number
     * @param order order
     * @return true if performed
     * @throws ServiceException while unable to perform a service action
     */
    boolean doPayment(String cardNumber, Order order) throws ServiceException;

    /**Check is given card number exists
     * @param cardNumber card number
     * @return true if exists
     * @throws ServiceException while unable to perform a service action
     */
    boolean checkCardExistence(String cardNumber) throws ServiceException;

    /**Check is given card has enough money
     * @param cardNumber card number
     * @param amount amount of money
     * @param isCredit is user took out a credit
     * @return true if enough money
     * @throws ServiceException while unable to perform a service action
     */
    boolean checkCardBalance(String cardNumber, BigDecimal amount, boolean isCredit) throws ServiceException;
}
