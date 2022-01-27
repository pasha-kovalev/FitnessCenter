package com.epam.jwd.fitness_center.controller.command;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

public interface CommandRequest {
    void addAttributeToJsp(String name, Object attribute);

    String getParameter(String name);

    String[] getParameterValues(String name);

    boolean sessionExists();

    void addToSession(String name, Object value);

    Optional<Object> retrieveFromSession(String name);

    Optional<Object> pullFromSession(String name);

    void removeFromSession(String name);

    void clearSession();

    void createSession();

    Optional<Part> getPart(String var);

    ServletContext getServletContext();
}
