package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ConfirmEmailCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ConfirmEmailCommand.class);
    private final UserService userService;
    private final RequestFactory requestFactory;

    ConfirmEmailCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        this.userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String tokenValue = request.getParameter(RequestParameter.TOKEN);
        request.removeFromSession(Attribute.ADDITIONAL_INFO);
        Optional<Long> optionalTokenId = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.TOKEN_ID);
        if (!optionalTokenId.isPresent()) {
            request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
            return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
        }
        try {
            if (!userService.confirmUser(optionalTokenId.get().intValue(), tokenValue)) {
                request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
                return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
            }
        } catch (ServiceException e) {
            LOG.error("Error during user confirmation", e);
            return requestFactory.createRedirectResponse(PagePath.ERROR500);
        }
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_VALID_LINK);
        return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }
}
