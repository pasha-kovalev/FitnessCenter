package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.MailService;

public class MailServiceImpl implements MailService {
    private static final int EXPIRATION = 60 * 24;
    private String token;

    @Override
    public boolean send(String to) {
        return false;
    }

    public boolean sendConfirmationEmail(User user, String token)
}
