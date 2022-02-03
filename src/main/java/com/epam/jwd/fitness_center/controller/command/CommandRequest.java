package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.controller.ResponseCreator;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Optional;

/** Interface of result of {@link ResponseCreator#createRequest(HttpServletRequest)} that wraps http request
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface CommandRequest {
    /** Sets attribute to request
     * @param name attribute name
     * @param attribute attribute
     */
    void addAttributeToJsp(String name, Object attribute);

    /** Retrieve parameter value from request
     * @param name parameter name
     * @return parameter value
     */
    String getParameter(String name);

    /** Retrieves array of values by parameter from request
     * @param name parameter name
     * @return array of parameter values
     */
    String[] getParameterValues(String name);

    /** Sets attribute to session
     * @param name attribute name
     * @param value attribute value
     */
    void addToSession(String name, Object value);

    /** Retrieves attribute from session
     * @param name attribute name
     * @return optional of attribute value
     */
    Optional<Object> retrieveFromSession(String name);

    /** Retrieves attribute from session and then remove it
     * @param name attribute name
     * @return optional of attribute value
     */
    Optional<Object> pullFromSession(String name);

    /** Removes attribute from session
     * @param name attribute name
     */
    void removeFromSession(String name);

    /**
     *Invalidates session if it not null
     */
    void clearSession();

    /**
     * Creates new session
     */
    void createSession();

    /** Gets optional of the Part with the given name.
     * @param var name
     * @return optional of the Part
     */
    Optional<Part> getPart(String var);

    /** Gets the servlet context to which this ServletRequest was last dispatched
     * @return servlet context
     */
    ServletContext getServletContext();
}
