package com.epam.jwd.fitness_center.command;

public enum CommandType {
    DEFAULT,
    LOGIN,
    MAIN_PAGE,
    USERS_PAGE,
    SHOW_LOGIN_PAGE;


    public static CommandType of(String name) {
        for (CommandType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return DEFAULT;
    }
}
