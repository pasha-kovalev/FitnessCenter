package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.controller.command.SessionAttribute;
import com.epam.jwd.fitness_center.exception.ServiceException;

public interface MailService {
    void sendConfirmationEmail(long id, String email, SessionAttribute locale) throws ServiceException;
}
