package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.controller.command.impl.CommandProvider;

public interface Command {
    CommandResponse execute(CommandRequest request);

    static Command of(String name) {
        return CommandProvider.getInstance().of(name);
    }
}
