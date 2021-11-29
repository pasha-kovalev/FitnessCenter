package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.SessionAttribute;
import com.epam.jwd.fitness_center.controller.PropertyContext;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import java.util.Optional;

public class LoginCommand implements Command {
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    public static final String ERROR_LOGIN_MSG = "Invalid login or password";
    public static final String LOGIN_REDIRECT_PAGE = "login_redirect";
    public static final String INDEX_PAGE = "index";

    private final UserService userService;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    LoginCommand(RequestFactory requestFactory,
                 PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        Optional<User> user = Optional.empty();
        try {
            user = userService.authenticate(login, password);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if(!user.isPresent() ) {
            request.addToSession(SessionAttribute.ERROR_LOGIN, ERROR_LOGIN_MSG);
            return requestFactory.createRedirectResponse(propertyContext.get(LOGIN_REDIRECT_PAGE));
        }
        request.clearSession();
        request.createSession();
        request.addToSession(SessionAttribute.USER, user.get());
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
