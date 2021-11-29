package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.fitness_center.model.entity.UserRole.*;

public enum CommandType {
    DEFAULT,
    MAIN_PAGE,
    LOGIN,
    SHOW_USERS(ADMIN),
    SHOW_LOGIN(GUEST),
    SHOW_ERROR,
    LOG_OUT(USER, TRAINER, ADMIN, REGULAR_USER, CORPORATE_USER),
    SHOW_SIGNUP(GUEST),
    SIGNUP;

    private final List<UserRole> allowedRoles;

    CommandType(UserRole... roles) {
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : UserRole.valuesAsList();
    }

    public List<UserRole> getAllowedRoles() {
        return allowedRoles;
    }

    public static CommandType of(String name) {
        for (CommandType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return DEFAULT;
    }
}
