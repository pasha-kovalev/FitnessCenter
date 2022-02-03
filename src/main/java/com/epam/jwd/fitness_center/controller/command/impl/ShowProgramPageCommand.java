package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.Program;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ShowProgramPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowProgramPageCommand.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;
    private final ProgramService programService;

    ShowProgramPageCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
        programService = ServiceProvider.getInstance().getProgramService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
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
            Optional<Program> optionalProgram;
            if (user.getRole() == UserRole.TRAINER) {
                if (!order.getAssignmentTrainerId().equals(user.getId()) && !order.getTrainerId().equals(user.getId())) {
                    return CommandHelper.createInfoErrorResponse(responseCreator, request);
                }
                optionalProgram = programService.find(orderId);
            } else {
                optionalProgram = programService.findByOrderAndClientId(orderId, user.getId());
            }
            if (!optionalProgram.isPresent()) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            request.addAttributeToJsp(Attribute.PROGRAM, optionalProgram.get());
            request.addAttributeToJsp(Attribute.ORDER, order);
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        return responseCreator.createForwardResponse(PagePath.SHOW_VIEW_EDIT_PROGRAM);
    }
}
