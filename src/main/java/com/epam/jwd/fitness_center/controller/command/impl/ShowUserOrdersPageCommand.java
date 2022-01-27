package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.model.entity.OrderStatus;
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

public class ShowUserOrdersPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowUserOrdersPageCommand.class);
    public static final String TRUE_STR = "true";
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    ShowUserOrdersPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Order> orders;
        String isCurrentOrdersParam = request.getParameter(RequestParameter.IS_CURRENT);
        Optional<UserDetails> userDetailsOptional = retrieveUserDetailsFromSession(request);
        if (!userDetailsOptional.isPresent() || isCurrentOrdersParam == null) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        UserDetails userDetails = userDetailsOptional.get();
        long userId = userDetails.getUserId();
        try {
            if(isCurrentOrdersParam.equals(TRUE_STR)) {
                orders = orderService.findOrderByUserIdAndStatus(userId, OrderStatus.ACTIVE,
                        OrderStatus.TAKEN, OrderStatus.PENDING_TRAINER, OrderStatus.PENDING_CLIENT,
                        OrderStatus.UNTAKEN, OrderStatus.PAYMENT_AWAITING);
            } else {
                orders = orderService.findOrderByUserIdAndStatus(userId, OrderStatus.CANCELLED, OrderStatus.COMPLETED);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(orders);
        return requestFactory.createAjaxResponse(json);
    }
}