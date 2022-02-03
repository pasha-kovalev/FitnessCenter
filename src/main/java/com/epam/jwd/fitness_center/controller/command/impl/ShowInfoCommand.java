package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;

public class ShowInfoCommand implements Command {
    private final ResponseCreator responseCreator;

    ShowInfoCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return responseCreator.createForwardResponse(PagePath.SHOW_INFO);
    }
}
