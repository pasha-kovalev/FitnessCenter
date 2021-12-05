package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.controller.command.SessionAttribute;
import com.epam.jwd.fitness_center.exception.ServiceException;

public interface MailService {
    long sendConfirmationEmail(long id, String email, String locale) throws ServiceException;
}
