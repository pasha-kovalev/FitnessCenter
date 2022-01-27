package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ManageNewDiscountCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ManageNewDiscountCommand.class);

    private final RequestFactory requestFactory;
    private final UserService userService;

    ManageNewDiscountCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<Long> userIdOptional = retrievePositiveLongParameter(request, RequestParameter.ID);
        String discountStr = request.getParameter(RequestParameter.DISCOUNT);
        if (!userIdOptional.isPresent() || discountStr == null) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        try {
            userService.updateUserDetailsDiscount(userIdOptional.get(), discountStr);
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return requestFactory.createAjaxResponse(json);
    }
}