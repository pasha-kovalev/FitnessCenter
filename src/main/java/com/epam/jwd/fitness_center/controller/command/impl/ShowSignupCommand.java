package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.RequestFactory;

public class ShowSignupCommand implements Command {
    private final RequestFactory requestFactory;

    ShowSignupCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.SIGNUP);
    }
}