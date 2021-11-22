package com.epam.jwd.fitness_center.command;

import com.epam.jwd.fitness_center.command.impl.CommandProvider;

public interface Command {
    CommandResponse execute(CommandRequest request);

    static Command of(String name) {
        return CommandProvider.getInstance().of(name);
    }
}
