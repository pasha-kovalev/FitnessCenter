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

public class ShowMakeProgramPageCommand implements Command {
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    ShowMakeProgramPageCommand(RequestFactory requestFactory) {
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
                Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
                Optional<Order> optionalOrder = orderService.findOrderById(orderId);
                if (!optionalOrder.isPresent() || !optionalUser.isPresent())  {
                    request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
                    return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
                }
                order = optionalOrder.get();
                request.addAttributeToJsp(Attribute.ORDER, order);
                if(order.getOrderStatus() == OrderStatus.TAKEN) {
                    return requestFactory.createForwardResponse(PagePath.SHOW_PROGRAM);
                }
                if(order.getOrderStatus() != OrderStatus.UNTAKEN) {
                    return requestFactory.createForwardResponse(PagePath.ERROR);
                }
                order.setAssignmentTrainerId(((User)optionalUser.get()).getId());
                order.setOrderStatus(OrderStatus.TAKEN);
                orderService.update(order);
            } catch (ServiceException e) {
                LOG.error("Error during order confirmation", e);
                return requestFactory.createForwardResponse(PagePath.ERROR500);
            }
        } else {
                return requestFactory.createForwardResponse(PagePath.ERROR);
        }

        return requestFactory.createForwardResponse(PagePath.SHOW_PROGRAM);
    }
}
