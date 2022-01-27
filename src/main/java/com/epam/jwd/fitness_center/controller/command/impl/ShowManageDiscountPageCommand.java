package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Entity;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowManageDiscountPageCommand implements Command {
    public static final String USERS_KEY = "users";
    public static final String USER_DETAILS_KEY = "userDetails";
    private static final Logger LOG = LogManager.getLogger(ShowManageDiscountPageCommand.class);
    private final RequestFactory requestFactory;
    private final UserService userService;

    ShowManageDiscountPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<User> users;
        List<UserDetails> userDetailsList;
        try {
            users = userService.findActiveClients();
            userDetailsList = userService.findAllUserDetails();
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createRedirectResponse(PagePath.ERROR500);
        }
        Map<String, List<? extends Entity>> response = new HashMap<>();
        response.put(USERS_KEY, users);
        response.put(USER_DETAILS_KEY, userDetailsList);
        String json = new Gson().toJson(response);
        return requestFactory.createAjaxResponse(json);
    }
}