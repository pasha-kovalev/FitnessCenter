package com.epam.jwd.fitness_center.controller.command;

public interface CommandResponse {
    boolean isRedirect();

    String getPath();
}
