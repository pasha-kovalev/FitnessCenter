package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.app.Application;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;

import java.util.List;

public enum ShowUsersPageCommand implements Command {
    INSTANCE;

    public static final String USERS_ATTRIBUTE_NAME = "users";
    public static final CommandResponse FORWARD_TO_USERS_PAGE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/users.jsp";
        }
    };

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<User> users;
        try {
            users = ServiceProvider.getInstance().getUserService().findAll();
        } catch (ServiceException e) {
            return ShowNotFoundPageCommand.INSTANCE.execute(request);
        }
        request.addAttributeToJsp(USERS_ATTRIBUTE_NAME, users);
        return FORWARD_TO_USERS_PAGE;
    }
}
