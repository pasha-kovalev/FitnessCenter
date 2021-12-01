package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SwitchLocaleCommand implements Command {

    private static final Logger LOG = LogManager.getLogger(SwitchLocaleCommand.class);

    private final RequestFactory requestFactory;

    SwitchLocaleCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        return null;
    }
}
