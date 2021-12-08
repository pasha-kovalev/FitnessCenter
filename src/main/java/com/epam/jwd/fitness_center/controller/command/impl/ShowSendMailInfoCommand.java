package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;

public class ShowSendMailInfoCommand implements Command {
    private final RequestFactory requestFactory;

    ShowSendMailInfoCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }
    //todo add to filter
    @Override
    public CommandResponse execute(CommandRequest request) {
        return requestFactory.createForwardResponse(PagePath.MAIL_INFO);
    }
}