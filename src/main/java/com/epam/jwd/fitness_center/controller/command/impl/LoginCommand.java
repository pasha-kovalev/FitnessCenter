package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    private final UserService userService;
    private final RequestFactory requestFactory;

    LoginCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        this.userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(RequestParameter.LOGIN);
        final String password = request.getParameter(RequestParameter.PASSWORD);
        Optional<User> user;
        try {
            user = userService.authenticate(login, password);
        } catch (ServiceException e) {
            LOG.error("Error during login", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        if(!user.isPresent() ) {
            request.addToSession(SessionAttribute.ERROR_LOGIN_BUNDLE_KEY, ResourceBundleKey.LOGIN_ERROR);
            return requestFactory.createRedirectResponse(PagePath.LOGIN_REDIRECT);
        }
        request.clearSession();
        request.createSession();
        request.addToSession(SessionAttribute.USER, user.get());
        return requestFactory.createRedirectResponse(PagePath.INDEX);
    }
}
