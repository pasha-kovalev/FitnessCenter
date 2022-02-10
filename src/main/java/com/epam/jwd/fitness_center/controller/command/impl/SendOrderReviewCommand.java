package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class SendOrderReviewCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SendOrderReviewCommand.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;

    SendOrderReviewCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String review = request.getParameter(RequestParameter.REVIEW);
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent() || review == null) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        Optional<UserDetails> userDetailsOptional = CommandHelper.retrieveUserDetailsFromSession(request);
        long orderId = orderIdOptional.get();
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!userDetailsOptional.isPresent() || !optionalOrder.isPresent()) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            long userId = userDetailsOptional.get().getUserId();
            Order order = optionalOrder.get();
            if (order.getOrderStatus() != OrderStatus.COMPLETED || order.getUserDetailsId() != userId ||
                    !orderService.addOrderReview(review, orderId)) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createAjaxResponse(json);
    }
}