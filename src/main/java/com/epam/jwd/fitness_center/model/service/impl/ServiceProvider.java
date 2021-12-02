package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.model.service.MailService;
import com.epam.jwd.fitness_center.model.service.UserService;

public final class ServiceProvider {
    private final UserService userService;
    private final MailService mailService;

    private ServiceProvider() {
        mailService = new MailServiceImpl();
        userService = new UserServiceImpl();
    }

    public static ServiceProvider getInstance() {
        return ServiceProvider.ServiceProviderHolder.instance;
    }

    public UserService getUserService() {
        return userService;
    }
    public MailService getMailService() {
        return mailService;
    }


    private static class ServiceProviderHolder {
        private static final ServiceProvider instance = new ServiceProvider();
    }

}
