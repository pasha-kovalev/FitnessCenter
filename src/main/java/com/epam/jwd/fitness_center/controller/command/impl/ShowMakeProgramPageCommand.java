package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ShowMakeProgramPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowMakeProgramPageCommand.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;

    ShowMakeProgramPageCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Order order;
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (orderIdOptional.isPresent()) {
            long orderId = orderIdOptional.get();
            try {
                Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
                Optional<Order> optionalOrder = orderService.findOrderById(orderId);
                if (!optionalOrder.isPresent() || !optionalUser.isPresent()) {
                    request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
                    return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
                }
                order = optionalOrder.get();
                request.addAttributeToJsp(Attribute.ORDER, order);
                if (order.getOrderStatus() == OrderStatus.TAKEN) {
                    return responseCreator.createForwardResponse(PagePath.SHOW_NEW_PROGRAM);
                }
                if (order.getOrderStatus() != OrderStatus.UNTAKEN) {
                    return responseCreator.createRedirectResponse(PagePath.ERROR);
                }
                order.setAssignmentTrainerId(((User) optionalUser.get()).getId());
                order.setOrderStatus(OrderStatus.TAKEN);
                orderService.update(order);
            } catch (ServiceException e) {
                LOG.error("Error during order confirmation", e);
                return responseCreator.createRedirectResponse(PagePath.ERROR500);
            }
        } else {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }

        return responseCreator.createForwardResponse(PagePath.SHOW_NEW_PROGRAM);
    }
}
