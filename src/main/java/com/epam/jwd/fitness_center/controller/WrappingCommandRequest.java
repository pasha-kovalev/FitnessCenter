package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class WrappingCommandRequest implements CommandRequest {
    private final HttpServletRequest request;

    public WrappingCommandRequest(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void addAttributeToJsp(String name, Object attribute) {
        request.setAttribute(name, attribute);
    }

    @Override
    public String getParameter(String name) {
        return request.getParameter(name);
    }

    @Override
    public boolean sessionExists() {
        return request.getSession(false) != null;
    }

    @Override
    public void addToSession(String name, Object value) {
        final HttpSession session = request.getSession(true);
        session.setAttribute(name, value);
    }

    @Override
    public Optional<Object> retrieveFromSession(String name) {
        return Optional.ofNullable(request.getSession(false))
                .map(session -> session.getAttribute(name));
    }

    @Override
    public void removeFromSession(String name) {
        final HttpSession session = request.getSession(false);
        session.removeAttribute(name);
    }

    public Optional<Object> pullFromSession(String name) {
        Optional<Object> result = retrieveFromSession(name);
        removeFromSession(name);
        return result;
    }

    @Override
    public void clearSession() {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    @Override
    public void createSession() {
        request.getSession(true);
    }
}
