package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.controller.listener.HttpSessionListenerImpl;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    private final UserService userService;
    private final ResponseCreator responseCreator;

    LoginCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        this.userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(RequestParameter.LOGIN);
        final String password = request.getParameter(RequestParameter.PASSWORD);
        Optional<User> optionalUser;
        UserDetails userDetails;
        try {
            optionalUser = userService.authenticate(login, password);
        } catch (ServiceException e) {
            LOG.error("Error during login", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        if (!optionalUser.isPresent()) {
            request.addToSession(Attribute.ERROR_LOGIN_BUNDLE_KEY, ResourceBundleKey.LOGIN_ERROR);
            return responseCreator.createRedirectResponse(PagePath.LOGIN_REDIRECT);
        }
        User user = optionalUser.get();
        if(user.getStatus() == UserStatus.BANNED) {
            request.addToSession(Attribute.ERROR_LOGIN_BUNDLE_KEY, ResourceBundleKey.BANNED_ERROR);
            return responseCreator.createRedirectResponse(PagePath.LOGIN_REDIRECT);
        }
        request.createSession();
        request.addToSession(Attribute.USER, user);
        HttpSessionListenerImpl.getSessionMap(request.getServletContext()).put(user.getId(), request.getSession());
        try {
            userDetails = userService.findUserDetails(user.getId());
        } catch (ServiceException e) {
            userDetails = null;
        }
        if (userDetails != null) {
            request.addToSession(Attribute.USER_DETAILS, userDetails);
            try {
                userService.findUserById(userDetails.getPersonalTrainerId())
                           .ifPresent(u -> request.addToSession(Attribute.TRAINER_EMAIL, u.getEmail()));
            } catch (ServiceException e) {
                LOG.error("Unable to find personal trainer for user with id {}", user.getId());
            }
        }
        return responseCreator.createRedirectResponse(PagePath.MAIN_REDIRECT);
    }
}
