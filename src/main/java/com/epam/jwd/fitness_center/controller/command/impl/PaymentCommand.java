package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.PaymentService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class PaymentCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(PaymentCommand.class);
    private final ResponseCreator responseCreator;
    private final PaymentService paymentService;
    private final OrderService orderService;

    PaymentCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        paymentService = ServiceProvider.getInstance().getPaymentService();
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> orderOptional = request.retrieveFromSession(Attribute.ORDER);
        if (!orderOptional.isPresent()) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        Order order = (Order) orderOptional.get();
        if (order.getOrderStatus() != OrderStatus.PAYMENT_AWAITING) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        String cardNumber = request.getParameter(RequestParameter.CARD_NUMBER);
        if (request.getParameter(RequestParameter.CREDIT_CHECKBOX) != null) {
            Optional<CommandResponse> optionalResponse = processCredit(request, order, cardNumber);
            if (optionalResponse.isPresent()) {
                return optionalResponse.get();
            }
        } else {
            Optional<CommandResponse> optionalResponse = processPayment(request, order, cardNumber);
            if (optionalResponse.isPresent()) {
                return optionalResponse.get();
            }
        }
        request.removeFromSession(Attribute.ORDER);
        request.removeFromSession(Attribute.CREDIT_PERCENTAGE);
        request.removeFromSession(Attribute.CREDIT_PERIOD);
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }

    private Optional<CommandResponse> processPayment(CommandRequest request, Order order, String cardNumber) {
        try {
            if (!paymentService.checkCardExistence(cardNumber)) {
                request.addToSession(Attribute.ERROR_PAYMENT_BUNDLE_KEY,
                        ResourceBundleKey.INFO_PAYMENT_CARD_NOT_EXIST);
                return Optional.of(responseCreator.createRedirectResponse(PagePath.SHOW_PAYMENT_REDIRECT));
            }
            if (!paymentService.checkCardBalance(cardNumber, order.getPrice(), false)) {
                request.addToSession(Attribute.ERROR_PAYMENT_BUNDLE_KEY,
                        ResourceBundleKey.INFO_PAYMENT_INSUFFICIENT_FUNDS);
                return Optional.of(responseCreator.createRedirectResponse(PagePath.SHOW_PAYMENT_REDIRECT));
            }
            orderService.updateOrderStatus(OrderStatus.UNTAKEN, order.getId());
            paymentService.doPayment(cardNumber, order);
            return Optional.empty();
        } catch (ServiceException e) {
            LOG.error("Error during payment processing", e);
            return Optional.of(responseCreator.createRedirectResponse(PagePath.ERROR500));
        }
    }

    private Optional<CommandResponse> processCredit(CommandRequest request, Order order, String cardNumber) {
        LOG.info("Credit selected");
        try {
            if (!paymentService.checkCardExistence(cardNumber)) {
                request.addToSession(Attribute.ERROR_PAYMENT_BUNDLE_KEY,
                        ResourceBundleKey.INFO_PAYMENT_CARD_NOT_EXIST);
                return Optional.of(responseCreator.createRedirectResponse(PagePath.SHOW_PAYMENT_REDIRECT));
            }
            if (!paymentService.checkCardBalance(cardNumber, order.getPrice(), true)) {
                request.addToSession(Attribute.ERROR_PAYMENT_BUNDLE_KEY,
                        ResourceBundleKey.INFO_PAYMENT_INSUFFICIENT_FUNDS);
                return Optional.of(responseCreator.createRedirectResponse(PagePath.SHOW_PAYMENT_REDIRECT));
            }
            orderService.updateOrderStatus(OrderStatus.UNTAKEN, order.getId());
            paymentService.establishCredit(cardNumber, order);
            return Optional.empty();
        } catch (ServiceException e) {
            LOG.error("Error during credit processing", e);
            return Optional.of(responseCreator.createRedirectResponse(PagePath.ERROR500));
        }
    }
}
