package com.epam.jwd.fitness_center.controller.command;

import java.util.Optional;

public interface CommandRequest {
    void addAttributeToJsp(String name, Object attribute);

    String getParameter(String name);

    boolean sessionExists();

    boolean addToSession(String name, Object value);

    Optional<Object> retrieveFromSession(String name);
    void removeFromSession(String name);
    void clearSession();

    void createSession();
}
