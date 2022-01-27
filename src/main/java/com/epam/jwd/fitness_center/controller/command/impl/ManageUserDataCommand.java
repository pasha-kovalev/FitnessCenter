package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ManageUserDataCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ManageUserDataCommand.class);

    private final RequestFactory requestFactory;
    private final UserService userService;

    ManageUserDataCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String[] fieldNames = request.getParameterValues(RequestParameter.NAME);
        String[] values = request.getParameterValues(RequestParameter.VALUE);
        Optional<Long> userIdOptional = retrievePositiveLongParameter(request, RequestParameter.USER_ID);
        if (!userIdOptional.isPresent() || fieldNames == null || values == null || fieldNames.length != values.length) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        long userId = userIdOptional.get();
        try {
            for (int i = 0; i < fieldNames.length; i++) {
                switch (fieldNames[i]) {
                    case "status":
                        userService.updateUserStatus(UserStatus.valueOf(values[i].toUpperCase()), userId);
                        break;
                    case "role":
                        userService.updateUserRole(UserRole.valueOf(values[i].toUpperCase()), userId);
                        break;
                    case "default":
                        LOG.warn("Not found field name: {}", fieldNames[i]);
                }
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return requestFactory.createAjaxResponse(json);
    }
}