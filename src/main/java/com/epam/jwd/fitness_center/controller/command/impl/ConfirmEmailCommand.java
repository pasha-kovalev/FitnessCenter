package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Token;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.epam.jwd.fitness_center.model.service.impl.UserServiceImpl;
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
        //todo unconfirmed user cant access
        String tokenIdStr = request.getParameter(RequestParameter.TOKEN_ID);
        String tokenValue = request.getParameter(RequestParameter.TOKEN);
            //todo validator
        int tokenId = Integer.parseInt(tokenIdStr);
        try {
            if (!userService.confirmUser(tokenId, tokenValue)) {
                request.addAttributeToJsp(RequestParameter.ERROR_CONFIRM_EMAIL, "Link is not valid");
                return requestFactory.createForwardResponse(PagePath.EMAIL_CONFIRMED_INFO);
            }
        } catch (ServiceException e) {
            LOG.error("Error during user confirmation", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        return requestFactory.createForwardResponse(PagePath.EMAIL_CONFIRMED_INFO);
    }

}
