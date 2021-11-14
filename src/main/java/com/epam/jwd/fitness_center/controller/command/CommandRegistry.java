package com.epam.jwd.fitness_center.controller.command;

public enum CommandRegistry {
    MAIN_PAGE(ShowMainPageCommand.INSTANCE, "main_page"),
    SHOW_USERS(ShowUsersPageCommand.INSTANCE, "show_users"),
    DEFAULT(ShowNotFoundPageCommand.INSTANCE, "");

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

