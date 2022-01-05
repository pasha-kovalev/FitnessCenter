package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Optional;

public class SignupCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(SignupCommand.class);

    private final UserService userService;
    private final RequestFactory requestFactory;

    SignupCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        this.userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String login = request.getParameter(RequestParameter.LOGIN);
        final String password = request.getParameter(RequestParameter.PASSWORD);
        final String passwordRepeat = request.getParameter(RequestParameter.PASSWORD_REPEAT);
        final String firstName = request.getParameter(RequestParameter.FIRSTNAME);
        final String lastname = request.getParameter(RequestParameter.LASTNAME);
        Optional<CommandResponse> response = registerUser(request, login, password, passwordRepeat, firstName, lastname);
        if (response.isPresent()) return response.get();
        request.clearSession();
        request.createSession();
        request.addToSession(SessionAttribute.LOGIN, login);
        return requestFactory.createRedirectResponse(PagePath.MAIL_INFO_REDIRECT);
    }

    private Optional<CommandResponse> registerUser(CommandRequest request, String login, String password,
                                                   String passwordRepeat, String firstName, String lastname) {
        Optional<User> user;
        try {
            user = userService.register(login, password, passwordRepeat, firstName, lastname, UserRole.USER,
                    UserStatus.UNCONFIRMED,
                    (String) request.retrieveFromSession(SessionAttribute.LOCALE)
                            .orElse(Locale.getDefault().toString()));
        } catch (ServiceException e) {
            LOG.error("Error during registering new user", e);
            return Optional.of(requestFactory.createForwardResponse(PagePath.ERROR500));
        }
        if (!user.isPresent()) {
            request.addToSession(SessionAttribute.ERROR_SIGNUP_BUNDLE_KEY, ResourceBundleKey.SIGNUP_ERROR);
            return Optional.of(requestFactory.createRedirectResponse(PagePath.SIGNUP_REDIRECT));
        }
        return Optional.empty();
    }
}
