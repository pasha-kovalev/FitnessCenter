package com.epam.jwd.fitness_center.command;

public enum CommandType {
    DEFAULT,
    LOGIN,
    MAIN_PAGE,
    SHOW_USERS,
    SHOW_LOGIN,
    LOG_OUT,
    SHOW_SIGNUP,
    SIGNUP;


    public static CommandType of(String name) {
        for (CommandType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return DEFAULT;
    }
}
