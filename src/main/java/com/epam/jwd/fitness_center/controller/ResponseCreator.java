package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandResponse;

/**
 * Interface of response creator
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface ResponseCreator {
    /**
     * Creates forward response
     *
     * @param page constant that represent page path
     * @return response
     */
    CommandResponse createForwardResponse(PagePath page);

    /**
     * Creates redirect response
     *
     * @param page constant that represent page path
     * @return response
     */
    CommandResponse createRedirectResponse(PagePath page);

    /**
     * Creates ajax response
     *
     * @param data data of ajax response
     * @return response
     */
    CommandResponse createAjaxResponse(String data);
}
