package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ShowProgramDetails implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowProgramDetails.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;
    private final ProgramService programService;

    ShowProgramDetails(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
        programService = ServiceProvider.getInstance().getProgramService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Program> optionalProgram;
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent()) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        long orderId = orderIdOptional.get();
        Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!optionalOrder.isPresent() || !optionalUser.isPresent()) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            User user = (User) optionalUser.get();
            Order order = optionalOrder.get();
            if (user.getRole() != UserRole.TRAINER) {
                optionalProgram = programService.findByOrderAndClientId(orderId, user.getId());
            } else {
                if (!order.getAssignmentTrainerId().equals(user.getId()) && !order.getTrainerId().equals(user.getId())) {
                    return CommandHelper.createInfoErrorResponse(responseCreator, request);
                }
                optionalProgram = programService.find(orderId);
            }
            if (!optionalProgram.isPresent() || !order.getOrderStatus().equals(OrderStatus.ACTIVE)) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(optionalProgram.get());
        return responseCreator.createAjaxResponse(json);
    }
}