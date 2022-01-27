package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;

import java.util.Optional;

public class CancelOrderCommand implements Command {
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    CancelOrderCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> orderIdOptional = retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent()) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
        long orderId = orderIdOptional.get();
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!optionalUser.isPresent() || !optionalOrder.isPresent())  {
                return createInfoErrorResponse(requestFactory, request);
            }
            Order order = optionalOrder.get();
            if(order.getOrderStatus() != OrderStatus.PAYMENT_AWAITING) {
                return createInfoErrorResponse(requestFactory, request);
            }
            orderService.updateOrderStatus(OrderStatus.CANCELLED, orderId);
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_SUCCESS);
        return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }
}
