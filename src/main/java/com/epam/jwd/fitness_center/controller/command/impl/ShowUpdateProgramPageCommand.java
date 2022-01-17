package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.Program;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;

import java.util.Optional;

public class ShowUpdateProgramPageCommand implements Command {
    private final RequestFactory requestFactory;
    private final OrderService orderService;
    private final ProgramService programService;

    ShowUpdateProgramPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
        programService = ServiceProvider.getInstance().getProgramService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> orderIdOptional = retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent()) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        long orderId = orderIdOptional.get();
        Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!optionalOrder.isPresent() || !optionalUser.isPresent())  {
                return createInfoErrorResponse(requestFactory, request);
            }
            User client = (User) optionalUser.get();
            Optional<Program> optionalProgram = programService.findByOrderAndClientId(orderId, client.getId());
            if(!optionalProgram.isPresent()) {
                return createInfoErrorResponse(requestFactory, request);
            }
            request.addAttributeToJsp(Attribute.PROGRAM, optionalProgram.get());
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        return requestFactory.createForwardResponse(PagePath.SHOW_PROGRAM);
    }
}
