package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;

/**
 * The interface represents mail service
 */
public interface MailService {
    /**
     * Sends sign up confirmation message to email
     *
     * @param userId userId
     * @param email  email
     * @param locale language of message
     * @return id of confirmation token
     * @throws ServiceException while unable to perform a service action
     */
    long sendConfirmationEmail(long userId, String email, String locale) throws ServiceException;
}
