package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.model.service.MailService;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.PaymentService;
import com.epam.jwd.fitness_center.model.service.UserService;

public final class ServiceProvider {
    private final UserService userService;
    private final MailService mailService;
    private final ItemServiceImpl itemService;
    private final OrderService orderService;
    private final PaymentService paymentService;

    private ServiceProvider() {
        mailService = new MailServiceImpl();
        userService = new UserServiceImpl();
        itemService = new ItemServiceImpl();
        orderService = new OrderServiceImpl();
        paymentService = new PaymentServiceImpl();
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

    public OrderService getOrderService() {
        return orderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    private static class ServiceProviderHolder {
        private static final ServiceProvider instance = new ServiceProvider();
    }

}
