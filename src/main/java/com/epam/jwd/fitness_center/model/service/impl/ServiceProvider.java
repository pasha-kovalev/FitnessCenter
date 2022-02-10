package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.model.service.*;

public final class ServiceProvider {
    private final UserService userService;
    private final MailService mailService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final ProgramService programService;

    private ServiceProvider() {
        mailService = new MailServiceImpl();
        userService = new UserServiceImpl();
        itemService = new ItemServiceImpl();
        orderService = new OrderServiceImpl();
        paymentService = new PaymentServiceImpl();
        programService = new ProgramServiceImpl();
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

    public ItemService getItemService() {
        return itemService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public ProgramService getProgramService() {
        return programService;
    }

    private static class ServiceProviderHolder {
        private static final ServiceProvider instance = new ServiceProvider();
    }

}
