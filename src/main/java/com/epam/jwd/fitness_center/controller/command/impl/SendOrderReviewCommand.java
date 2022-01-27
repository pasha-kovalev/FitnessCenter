package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
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

import java.util.List;
import java.util.Optional;

public class SendOrderReviewCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SendOrderReviewCommand.class);
    private final RequestFactory requestFactory;
    private final OrderService orderService;

    SendOrderReviewCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String review = request.getParameter(RequestParameter.REVIEW);
        Optional<Long> orderIdOptional = retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent() || review == null) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        Optional<UserDetails> userDetailsOptional = retrieveUserDetailsFromSession(request);
        long orderId = orderIdOptional.get();
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!userDetailsOptional.isPresent() || !optionalOrder.isPresent())  {
                return createInfoErrorResponse(requestFactory, request);
            }
            long userId = userDetailsOptional.get().getUserId();
            Order order = optionalOrder.get();
            if(order.getOrderStatus() != OrderStatus.COMPLETED || order.getUserDetailsId() != userId ||
                    !orderService.addOrderReview(review, orderId)) {
                return createInfoErrorResponse(requestFactory, request);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_SUCCESS);
        return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }
}