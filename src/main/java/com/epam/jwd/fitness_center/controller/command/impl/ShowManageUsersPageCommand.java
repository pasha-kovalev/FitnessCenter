package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandHelper;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ShowManageUsersPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowManageUsersPageCommand.class);
    private final ResponseCreator responseCreator;
    private final UserService userService;

    ShowManageUsersPageCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<User> users;
        Optional<User> userOptional = CommandHelper.retrieveUserFromSession(request);
        if (!userOptional.isPresent()) return responseCreator.createRedirectResponse(PagePath.ERROR);
        try {
            users = userService.findAll();
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        users.removeIf(u -> u.getRole() == UserRole.ADMIN);
        String json = new Gson().toJson(users);
        return responseCreator.createAjaxResponse(json);
    }
}