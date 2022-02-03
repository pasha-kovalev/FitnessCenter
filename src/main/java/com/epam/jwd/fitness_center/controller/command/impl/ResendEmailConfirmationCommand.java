package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.MailService;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;
import java.util.Optional;

public class ResendEmailConfirmationCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ResendEmailConfirmationCommand.class);

    private final UserService userService;
    private final MailService mailService;
    private final ResponseCreator responseCreator;

    ResendEmailConfirmationCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        this.userService = ServiceProvider.getInstance().getUserService();
        this.mailService = ServiceProvider.getInstance().getMailService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Object> loginObj = request.pullFromSession(RequestParameter.LOGIN);
        Optional<User> optionalUser;
        if (!loginObj.isPresent()) {
            LOG.error("Error during pull login from session");
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        final String login = (String) loginObj.get();
        try {
            optionalUser = userService.findUserByEmail(login);
        } catch (ServiceException e) {
            LOG.error("Error during searching user by email", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        if (!optionalUser.isPresent()) {
            request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_USER_NOT_FOUND);
            return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
        }

        User user = optionalUser.get();
        if (user.getStatus() != UserStatus.UNCONFIRMED) {
            request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_USER_CONFIRMED);
            return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
        }

        try {
            mailService.sendConfirmationEmail(user.getId(), user.getEmail(),
                    (String) request.retrieveFromSession(RequestParameter.LOCALE)
                            .orElse(Locale.getDefault().toString()));
        } catch (ServiceException e) {
            LOG.error("Error during sendConfirmationEmail", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_EMAIL_RESEND);
        request.addToSession(Attribute.ADDITIONAL_INFO, login);
        return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }
}