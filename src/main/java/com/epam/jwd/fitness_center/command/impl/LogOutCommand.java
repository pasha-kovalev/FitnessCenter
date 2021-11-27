package com.epam.jwd.fitness_center.command.impl;

import com.epam.jwd.fitness_center.command.Command;
import com.epam.jwd.fitness_center.command.CommandRequest;
import com.epam.jwd.fitness_center.command.CommandResponse;
import com.epam.jwd.fitness_center.command.SessionAttribute;
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
