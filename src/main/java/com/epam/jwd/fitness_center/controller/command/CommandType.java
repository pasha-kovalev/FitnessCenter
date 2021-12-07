package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.fitness_center.model.entity.UserRole.*;

public enum CommandType {
    DEFAULT,
    MAIN_PAGE,
    LOGIN(GUEST),
    SHOW_USERS(ADMIN),
    SHOW_LOGIN(GUEST),
    SHOW_ERROR,
    LOG_OUT(USER, TRAINER, ADMIN, REGULAR_USER, CORPORATE_USER),
    SHOW_SIGNUP(GUEST),
    SIGNUP(GUEST),
    SHOW_MAIL_INFO(GUEST),
    RESEND_EMAIL(GUEST),
    CONFIRM_EMAIL(GUEST),
    SHOW_CONFIRM_EMAIL(GUEST),
    SWITCH_LOCALE,
    SHOW_INFO,
    SHOW_ABOUT,
    SHOW_PROGRAMS,
    SHOW_TRANSFORM_PROGRAM,
    USER_CABINET(USER, CORPORATE_USER, REGULAR_USER),
    SHOW_ORDER(USER, CORPORATE_USER, REGULAR_USER);

    private final List<UserRole> allowedRoles;

    CommandType(UserRole... roles) {
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : UserRole.valuesAsList();
        Arrays.asList(USER, TRAINER, ADMIN, REGULAR_USER);
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
