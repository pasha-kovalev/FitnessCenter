package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.RequestParameter;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ShowTrainerOrdersPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowTrainerOrdersPageCommand.class);
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    ShowTrainerOrdersPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Order> orders;
        Optional<User> userOptional = retrieveUserFromSession(request);
        String orderStatusesValue = request.getParameter(RequestParameter.ORDER_STATUSES);
        if (!userOptional.isPresent()) return requestFactory.createRedirectResponse(PagePath.ERROR);
        User user = userOptional.get();
        try {
            if (orderStatusesValue.equalsIgnoreCase(OrderStatus.ACTIVE.name())) {
                orders = orderService.findOrderByAssignmentTrainerIdAndStatus(user.getId(), OrderStatus.ACTIVE,
                        OrderStatus.PENDING_TRAINER,
                        OrderStatus.PENDING_CLIENT,
                        OrderStatus.TAKEN);

            } else if (orderStatusesValue.equalsIgnoreCase(OrderStatus.UNTAKEN.name())) {
                orders = orderService.findOrderByStatus(OrderStatus.UNTAKEN);
            } else {
                orders = orderService.findOrderByAssignmentTrainerIdAndStatus(user.getId(), OrderStatus.COMPLETED,
                        OrderStatus.CANCELLED);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createRedirectResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(orders);
        return requestFactory.createAjaxResponse(json);
    }
}