package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class CancelOrderCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(CancelOrderCommand.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;

    CancelOrderCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent()) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
        long orderId = orderIdOptional.get();
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!optionalUser.isPresent() || !optionalOrder.isPresent()) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            Order order = optionalOrder.get();
            if (order.getOrderStatus() != OrderStatus.PAYMENT_AWAITING) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            orderService.updateOrderStatus(OrderStatus.CANCELLED, orderId);
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createAjaxResponse(json);
    }
}
