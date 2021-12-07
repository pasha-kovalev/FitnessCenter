package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.model.service.MailService;
import com.epam.jwd.fitness_center.model.service.UserService;

public final class ServiceProvider {
    private final UserService userService;
    private final MailService mailService;
    private final ItemServiceImpl itemService;

    private ServiceProvider() {
        mailService = new MailServiceImpl();
        userService = new UserServiceImpl();
        itemService = new ItemServiceImpl();
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

    public ItemServiceImpl getItemService() {
        return itemService;
    }


    private static class ServiceProviderHolder {
        private static final ServiceProvider instance = new ServiceProvider();
    }

}
