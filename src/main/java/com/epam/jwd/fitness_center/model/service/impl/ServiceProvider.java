package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.model.service.MailService;
import com.epam.jwd.fitness_center.model.service.UserService;

public class ServiceProvider {
    private final UserService userService = new UserServiceImpl();
    private final MailService mailService = new MailServiceImpl();

    private ServiceProvider() {
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
