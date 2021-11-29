package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.PropertyContext;
import com.epam.jwd.fitness_center.controller.RequestFactory;

public class ShowSignupCommand implements Command {

    public static final String SIGNUP_PAGE = "signup";
    private final RequestFactory requestFactory;
    private final PropertyContext propertyContext;

    ShowSignupCommand(RequestFactory requestFactory, PropertyContext propertyContext) {
        this.requestFactory = requestFactory;
        this.propertyContext = propertyContext;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(propertyContext.get(SIGNUP_PAGE));
    }
}