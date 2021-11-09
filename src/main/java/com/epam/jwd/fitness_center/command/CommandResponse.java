package com.epam.jwd.fitness_center.command;

public interface CommandResponse {
    boolean isRedirect();

    String getPath();
}
