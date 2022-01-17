package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.PaymentService;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MakeOrderCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(MakeOrderCommand.class);
    private static final long DEFAULT_PERIOD = 1L;


    private final OrderService orderService;
    private final UserService userService;
    private final RequestFactory requestFactory;

    MakeOrderCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        this.orderService = ServiceProvider.getInstance().getOrderService();
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> itemIdOptional, trainerIdOptional, periodOptional;
        Optional<UserDetails> userDetailsOptional = retrieveUserDetailsFromSession(request);
        if (!userDetailsOptional.isPresent()) return requestFactory.createForwardResponse(PagePath.ERROR);
        UserDetails userDetails = userDetailsOptional.get();
        itemIdOptional = parseLongRequestParameter(request, RequestParameter.PROGRAM);
        periodOptional = parseLongRequestParameter(request, RequestParameter.PERIOD);
        if (!itemIdOptional.isPresent()) return requestFactory.createForwardResponse(PagePath.ERROR);
        if (!periodOptional.isPresent()) periodOptional = Optional.of(DEFAULT_PERIOD);
        if (request.getParameter("trainer") != null) {
            trainerIdOptional = parseLongRequestParameter(request, Attribute.TRAINER);
            if (!trainerIdOptional.isPresent()) return requestFactory.createForwardResponse(PagePath.ERROR);
            try {
                userService.updateUserStatus(UserStatus.ACTIVE, userDetails.getUserId());
                userService.updateUserDetailsTrainerId(userDetails, trainerIdOptional.get());
            } catch (ServiceException e) {
                LOG.error(e);
                return requestFactory.createForwardResponse(PagePath.ERROR500);
            }
        }
        Order order;
        try {
            order = orderService.insert(userDetails.getUserId(), OrderStatus.PAYMENT_AWAITING, itemIdOptional.get(),
                    userDetails.getPersonalTrainerId(), periodOptional.get(), composeCommentForTrainer(request));
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addToSession(Attribute.ORDER, order);
        request.addToSession(Attribute.CREDIT_PERCENTAGE, PaymentService.DEFAULT_CREDIT_PERCENTAGE);
        request.addToSession(Attribute.CREDIT_PERIOD, PaymentService.DEFAULT_CREDIT_PERIOD);
        return requestFactory.createRedirectResponse(PagePath.SHOW_PAYMENT_REDIRECT);
    }

    private String composeCommentForTrainer(CommandRequest request) {
        String delimiter = ": ";
        return RequestParameter.WEIGHT + delimiter + request.getParameter(RequestParameter.WEIGHT) + '\n'
                + RequestParameter.HEIGHT + delimiter + request.getParameter(RequestParameter.HEIGHT) + '\n'
                + RequestParameter.COMMENT + delimiter + request.getParameter(RequestParameter.COMMENT);
    }

    private Optional<Long> parseLongRequestParameter(CommandRequest request, String parameter) {
        Optional<Long> result;
        String paramValue = request.getParameter(parameter);
        if (paramValue == null) {
            LOG.warn("Unable to parse order params: {}, because value is null.", parameter);
            return Optional.empty();
        }
        try {
            result = Optional.of(Long.parseLong(paramValue));
        } catch (NumberFormatException e) {
            LOG.warn("Unable to parse order params: {}, value: {}. {}", parameter, paramValue, e.getMessage());
            return Optional.empty();
        }
        return result;
    }
}
