package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandResponse;

/** Interface of request factory
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface ResponseCreator {
    /** Returns the instance of a concrete class
     * @return instance of class that implements the interface
     */
    static ResponseCreator getInstance() {
        return SimpleResponseCreator.getInstance();
    }

    /** Creates forward response
     * @param page constant that represent page path
     * @return response
     */
    CommandResponse createForwardResponse(PagePath page);

    /**Creates redirect response
     * @param page constant that represent page path
     * @return response
     */
    CommandResponse createRedirectResponse(PagePath page);

    /**Creates ajax response
     * @param data data of ajax response
     * @return response
     */
    CommandResponse createAjaxResponse(String data);
}
