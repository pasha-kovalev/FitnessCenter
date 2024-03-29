package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class EditUserDataCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(EditUserDataCommand.class);

    private final ResponseCreator responseCreator;
    private final UserService userService;

    EditUserDataCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String fieldName = request.getParameter(RequestParameter.NAME);
        String value = request.getParameter(RequestParameter.VALUE);
        Optional<User> userOptional = CommandHelper.retrieveUserFromSession(request);
        if (!userOptional.isPresent() || fieldName == null || value == null) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        User user = userOptional.get();
        try {
            userService.updateUserData(user.getId(), fieldName, value);
            request.addToSession(Attribute.USER, userService.findUserById(user.getId())
                    .orElseThrow(() -> new ServiceException("User not found")));
        } catch (ServiceException e) {
            LOG.error("Unable to edit user data", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createAjaxResponse(json);
    }
}