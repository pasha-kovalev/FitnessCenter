package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.epam.jwd.fitness_center.model.validator.NumberValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        //todo unconfirmed user cant access
        String tokenIdStr = request.getParameter(RequestParameter.TOKEN_ID);
        String tokenValue = request.getParameter(RequestParameter.TOKEN);
        //todo validation on services
        if(!NumberValidator.isPositiveInteger(tokenIdStr)) {
            request.addToSession(SessionAttribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
            return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
        }
        int tokenId = Integer.parseInt(tokenIdStr);
        try {
            if (!userService.confirmUser(tokenId, tokenValue)) {
                request.addToSession(SessionAttribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
                return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
            }
        } catch (ServiceException e) {
            LOG.error("Error during user confirmation", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addToSession(SessionAttribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_VALID_LINK);
        return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }
}
