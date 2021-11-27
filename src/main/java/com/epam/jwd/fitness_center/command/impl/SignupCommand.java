package com.epam.jwd.fitness_center.command.impl;

import com.epam.jwd.fitness_center.command.Command;
import com.epam.jwd.fitness_center.command.CommandRequest;
import com.epam.jwd.fitness_center.command.CommandResponse;
import com.epam.jwd.fitness_center.command.SessionAttribute;
import com.epam.jwd.fitness_center.controller.PropertyContext;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class SignupCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SignupCommand.class);
    private static final String LOGIN_REQUEST_PARAM_NAME = "login";
    private static final String PASSWORD_REQUEST_PARAM_NAME = "password";
    public static final String FIRSTNAME_REQUEST_PARAM_NAME = "firstname";
    public static final String LASTNAME_REQUEST_PARAM_NAME = "lastname";
    public static final String USER_EXISTS_MSG = "user with specified email already exists";
    public static final String SIGNUP_REDIRECT_PAGE = "signup_redirect";
    public static final String INDEX_PAGE = "index";
    public static final String ERROR_500_PAGE_NAME = "error500";


    private final UserService userService;
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    SignupCommand(RequestFactory requestFactory,
                 PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
        this.userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(LOGIN_REQUEST_PARAM_NAME);
        final String password = request.getParameter(PASSWORD_REQUEST_PARAM_NAME);
        final String firstName = request.getParameter(FIRSTNAME_REQUEST_PARAM_NAME);
        final String lastname = request.getParameter(LASTNAME_REQUEST_PARAM_NAME);
        Optional<User> user = Optional.empty();
        try {
            user = userService.register(login, password, firstName,lastname, UserRole.USER, UserStatus.INACTIVE);
        } catch (ServiceException e) {
            LOG.error("Error during registering new user", e);
            return requestFactory.createForwardResponse(propertyContext.get(ERROR_500_PAGE_NAME));
        }
        if(!user.isPresent()) {
            request.addToSession(SessionAttribute.ERROR_SIGNUP, USER_EXISTS_MSG);
            return requestFactory.createRedirectResponse(propertyContext.get(SIGNUP_REDIRECT_PAGE));
        }
        request.clearSession();
        request.createSession();
        //request.addToSession(SessionAttribute.USER, user.get());
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }
}
