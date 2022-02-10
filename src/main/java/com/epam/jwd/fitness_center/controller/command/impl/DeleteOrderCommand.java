package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteOrderCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(DeleteOrderCommand.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;

    DeleteOrderCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ID);
        boolean isDeleted;
        if (!orderIdOptional.isPresent()) {
            return CommandHelper.createInfoErrorResponse(responseCreator, request);
        }
        try {
            isDeleted = orderService.delete(orderIdOptional.get());
        } catch (ServiceException e) {
            LOG.error("Unable to delete order", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(isDeleted);
        return responseCreator.createAjaxResponse(json);
    }
}