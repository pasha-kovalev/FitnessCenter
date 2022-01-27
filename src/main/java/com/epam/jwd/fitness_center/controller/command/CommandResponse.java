package com.epam.jwd.fitness_center.controller.command;

public interface CommandResponse {
    boolean isRedirect();

    boolean isAjax();

    String getPath();

    String getAjaxData();
}
