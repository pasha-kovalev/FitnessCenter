package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class DeleteUserCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(DeleteUserCommand.class);
    private final ResponseCreator responseCreator;
    private final UserService userService;

    DeleteUserCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> userIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ID);
        boolean isDeleted;
        if (!userIdOptional.isPresent()) {
            return CommandHelper.createInfoErrorResponse(responseCreator, request);
        }
        try {
            isDeleted = userService.deleteUser(userIdOptional.get());
        } catch (ServiceException e) {
            LOG.error("Unable to delete user", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(isDeleted);
        return responseCreator.createAjaxResponse(json);
    }
}