package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;

public class DefaultCommand implements Command {
    private final ResponseCreator responseCreator;

    DefaultCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return responseCreator.createRedirectResponse(PagePath.MAIN_REDIRECT);
    }
}
