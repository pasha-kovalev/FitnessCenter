package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
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
        Optional<User> optionalUser;
        Optional<UserDetails> userDetailsOptional;
        try {
            optionalUser = userService.authenticate(login, password);
        } catch (ServiceException e) {
            LOG.error("Error during login", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        if (!optionalUser.isPresent()) {
            request.addToSession(Attribute.ERROR_LOGIN_BUNDLE_KEY, ResourceBundleKey.LOGIN_ERROR);
            return requestFactory.createRedirectResponse(PagePath.LOGIN_REDIRECT);
        }
        User user = optionalUser.get();
        request.clearSession();
        request.createSession();
        request.addToSession(Attribute.USER, user);
        try {
            userDetailsOptional = userService.findUserDetails(user.getId());
        } catch (ServiceException e) {
            LOG.error("Error during searching user details. User id: {}. {}", user.getId(), e.getMessage());
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        userDetailsOptional.ifPresent(details -> request.addToSession(Attribute.USER_DETAILS, details));
        return requestFactory.createRedirectResponse(PagePath.INDEX);
    }
}
