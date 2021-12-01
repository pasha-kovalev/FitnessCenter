package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

public interface RequestFactory {
    CommandRequest createRequest(HttpServletRequest request);

    CommandResponse createForwardResponse(PagePath page);

    CommandResponse createRedirectResponse(PagePath page);

    static RequestFactory getInstance() {
        return SimpleRequestFactory.getInstance();
    }
}
