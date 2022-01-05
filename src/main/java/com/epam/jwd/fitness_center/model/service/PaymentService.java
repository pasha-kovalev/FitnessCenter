package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;

import java.math.BigDecimal;

public interface PaymentService {
    BigDecimal DEFAULT_CREDIT_PERCENTAGE = BigDecimal.valueOf(5);
    int DEFAULT_CREDIT_PERIOD = 3;

    void establishCredit(String cardNumber, Order order)
            throws ServiceException;

    boolean doPayment(String cardNumber, Order order) throws ServiceException;

    boolean checkCardExistence(String cardNumber) throws ServiceException;

    boolean checkCardBalance(String cardNumber, BigDecimal amount, boolean isCredit) throws ServiceException;
}
