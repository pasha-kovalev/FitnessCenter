package com.epam.jwd.fitness_center.model.entity;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
    ADMIN, TRAINER, USER,  CORPORATE_USER, REGULAR_USER, GUEST;

    private static final List<UserRole> ALL_AVAILABLE_ROLES = Arrays.asList(values());

    public static List<UserRole> valuesAsList() {
        return ALL_AVAILABLE_ROLES;
    }

    public static UserRole of(String name) {
        for (UserRole role : values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        return USER;
    }
}
