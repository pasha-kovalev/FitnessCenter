package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.controller.RequestFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Optional;

/** Interface of result of {@link RequestFactory#createRequest(HttpServletRequest)} that wraps http request
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface CommandRequest {
    /** Set attribute to request
     * @param name attribute name
     * @param attribute attribute
     */
    void addAttributeToJsp(String name, Object attribute);

    /** Retrieve parameter value from request
     * @param name parameter name
     * @return parameter value
     */
    String getParameter(String name);

    /** Retrieve array of values by parameter from request
     * @param name parameter name
     * @return array of parameter values
     */
    String[] getParameterValues(String name);

    /** Set attribute to session
     * @param name attribute name
     * @param value attribute value
     */
    void addToSession(String name, Object value);

    /** Retrieve attribute from session
     * @param name attribute name
     * @return optional of attribute value
     */
    Optional<Object> retrieveFromSession(String name);

    /** Retrieve attribute from session and then remove it
     * @param name attribute name
     * @return optional of attribute value
     */
    Optional<Object> pullFromSession(String name);

    /** Remove attribute from session
     * @param name attribute name
     */
    void removeFromSession(String name);

    /**
     *Invalidate session if it not null
     */
    void clearSession();

    /**
     * Creates new session
     */
    void createSession();

    /** Gets optional of the Part with the given name.
     * @param var
     * @return optional of the Part
     */
    Optional<Part> getPart(String var);

    /** Gets the servlet context to which this ServletRequest was last dispatched
     * @return servlet context
     */
    ServletContext getServletContext();
}
