package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

/** Interface of request factory
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface RequestFactory {
    /** Returns the instance of a concrete class
     * @return instance of class that implements the interface
     */
    static RequestFactory getInstance() {
        return SimpleRequestFactory.getInstance();
    }

    /** Creates request wrapped in other class
     * @param request http request
     * @return request wrapped in CommandRequest
     */
    CommandRequest createRequest(HttpServletRequest request);

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
