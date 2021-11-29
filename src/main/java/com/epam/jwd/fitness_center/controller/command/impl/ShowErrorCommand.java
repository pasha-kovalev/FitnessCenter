package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PropertyContext;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.PagePath;

public class ShowErrorCommand implements Command {
    public static final String SHOW_ERROR_PAGE = "show_error";
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    ShowErrorCommand(RequestFactory requestFactory,
                  PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(SHOW_ERROR_PAGE));
    }
}
