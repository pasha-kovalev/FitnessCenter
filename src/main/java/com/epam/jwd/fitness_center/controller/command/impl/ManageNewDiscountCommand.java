package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
//todo jstl formatting for num internalization
public class ManageNewDiscountCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ManageNewDiscountCommand.class);

    private final ResponseCreator responseCreator;
    private final UserService userService;

    ManageNewDiscountCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        //todo admin can add discount to all corporate users at once
        Optional<Long> userIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ID);
        String discountStr = request.getParameter(RequestParameter.DISCOUNT);
        if (!userIdOptional.isPresent() || discountStr == null) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        try {
            userService.updateUserDetailsDiscount(userIdOptional.get(), discountStr);
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createAjaxResponse(json);
    }
}