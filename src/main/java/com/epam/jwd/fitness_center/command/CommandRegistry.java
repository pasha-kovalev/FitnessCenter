package com.epam.jwd.fitness_center.command;

import com.epam.jwd.fitness_center.command.impl.*;

public enum CommandRegistry {

    MAIN_PAGE(CommandProvider.getInstance().of(CommandType.MAIN_PAGE.name()), "main_page"),
    SHOW_USERS(CommandProvider.getInstance().of(CommandType.USERS_PAGE.name()), "show_users"),
    SHOW_LOGIN(CommandProvider.getInstance().of(CommandType.SHOW_LOGIN_PAGE.name()), "show_login"),
    LOGIN(CommandProvider.getInstance().of(CommandType.LOGIN.name()), "login"),
    DEFAULT(CommandProvider.getInstance().of(CommandType.DEFAULT.name()), "");

    private final Command command;
    private final String path;

    CommandRegistry(Command command, String path) {
        this.command = command;
        this.path = path;
    }

    public Command getCommand() {
        return command;
    }

    static Command of(String name) {
        for (CommandRegistry constant : values()) {
            if (constant.path.equalsIgnoreCase(name)) {
                return constant.command;
            }
        }
        return DEFAULT.command;
    }
}

