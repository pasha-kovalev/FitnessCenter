package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.PropertyContext;
import com.epam.jwd.fitness_center.controller.RequestFactory;

public class LogOutCommand implements Command {
    public static final String INDEX_PAGE = "index";

    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    LogOutCommand(RequestFactory requestFactory,
                 PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        request.clearSession();
        return requestFactory.createRedirectResponse(propertyContext.get(INDEX_PAGE));
    }

}
