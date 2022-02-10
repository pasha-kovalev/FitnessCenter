package com.epam.jwd.fitness_center.model.entity;

public enum UserStatus {
    ACTIVE, INACTIVE, BANNED, UNCONFIRMED;

    public static UserStatus of(String name) {
        for (UserStatus status : values()) {
            if (status.name().equalsIgnoreCase(name)) {
                return status;
            }
        }
        return BANNED;
    }
}
