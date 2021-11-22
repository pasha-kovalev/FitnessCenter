package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.command.CommandRequest;
import com.epam.jwd.fitness_center.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {
    CommandRequest createRequest(HttpServletRequest request);

    CommandResponse createForwardResponse(String path);

    CommandResponse createRedirectResponse(String path);

    static RequestFactory getInstance() {
        return SimpleRequestFactory.getInstance();
    }
}
