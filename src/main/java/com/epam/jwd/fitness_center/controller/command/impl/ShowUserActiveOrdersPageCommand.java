package com.epam.jwd.fitness_center.controller.command.impl;

import com.google.gson.Gson;
import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ShowUserActiveOrdersPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowUserActiveOrdersPageCommand.class);
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    ShowUserActiveOrdersPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Order> orders;
        Optional<UserDetails> userDetailsOptional = retrieveUserDetailsFromSession(request);
        if (!userDetailsOptional.isPresent()) return requestFactory.createForwardResponse(PagePath.ERROR);
        UserDetails userDetails = userDetailsOptional.get();
        try {
            //todo: ONLY active orders
            orders = orderService.findOrderByUserId(userDetails.getUserId());
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(orders);
        return requestFactory.createAjaxResponse(json);
    }
}