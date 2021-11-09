package com.epam.jwd.fitness_center.command;

import com.epam.jwd.fitness_center.app.Application;
import com.epam.jwd.fitness_center.dao.User;

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
        List<User> users = Application.extractUsersFromDb();

        request.addAttributeToJsp(USERS_ATTRIBUTE_NAME, users);
        return FORWARD_TO_USERS_PAGE;
    }
}
