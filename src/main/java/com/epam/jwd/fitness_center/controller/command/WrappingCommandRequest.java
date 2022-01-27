package com.epam.jwd.fitness_center.controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.Optional;

public class WrappingCommandRequest implements CommandRequest {
    private static final Logger LOG = LogManager.getLogger(WrappingCommandRequest.class);
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
    public String[] getParameterValues(String name) {
        return request.getParameterValues(name);
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

    @Override
    public Optional<Part> getPart(String var) {
        Part part = null;
        try {
            part = request.getPart(var);
        } catch (IOException | ServletException e) {
            LOG.error("Unable to read image. {}", e.getMessage());
        }
        return Optional.ofNullable(part);
    }

    @Override
    public ServletContext getServletContext() {
        return request.getServletContext();
    }
}
