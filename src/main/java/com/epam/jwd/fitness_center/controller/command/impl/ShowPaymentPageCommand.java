package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.SessionAttribute;

public class ShowPaymentPageCommand implements Command {
    private final RequestFactory requestFactory;

    ShowPaymentPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        if(!request.retrieveFromSession(SessionAttribute.ORDER).isPresent()) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        return requestFactory.createForwardResponse(PagePath.SHOW_PAYMENT);
    }
}
