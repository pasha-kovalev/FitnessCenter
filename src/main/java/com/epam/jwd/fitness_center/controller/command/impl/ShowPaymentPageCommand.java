package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;

import java.util.Optional;

public class ShowPaymentPageCommand implements Command {
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    ShowPaymentPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Order order;
        Optional<Long> orderIdOptional = retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (orderIdOptional.isPresent()) {
            long orderId = orderIdOptional.get();
            try {
                Optional<Order> optionalOrder = orderService.findOrderById(orderId);
                Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
                if (!optionalOrder.isPresent() || !optionalUser.isPresent()
                        || !optionalOrder.get().getUserDetailsId().equals(((User) optionalUser.get()).getId())) {
                    request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
                    return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
                }
                order = optionalOrder.get();
                request.addToSession(Attribute.ORDER, order);
            } catch (ServiceException e) {
                LOG.error("Error during order confirmation", e);
                return requestFactory.createForwardResponse(PagePath.ERROR500);
            }
        } else {
            Optional<Object> optionalOrder = request.retrieveFromSession(Attribute.ORDER);
            if (!optionalOrder.isPresent()) {
                return requestFactory.createForwardResponse(PagePath.ERROR);
            }
            order = (Order) optionalOrder.get();
        }
        if (order.getOrderStatus() != OrderStatus.PAYMENT_AWAITING) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        return requestFactory.createForwardResponse(PagePath.SHOW_PAYMENT);
    }
}
