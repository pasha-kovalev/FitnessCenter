package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRequestFactory implements RequestFactory {
    private final Map<String, CommandResponse> forwardResponseCache = new ConcurrentHashMap<>();
    private final Map<String, CommandResponse> redirectResponseCache = new ConcurrentHashMap<>();

    private SimpleRequestFactory() {
    }

    public static SimpleRequestFactory getInstance() {
        return SimpleRequestFactoryHolder.instance;
    }

    @Override
    public CommandRequest createRequest(HttpServletRequest request) {
        return new WrappingCommandRequest(request);
    }

    @Override
    public CommandResponse createForwardResponse(String path) {
        return forwardResponseCache.computeIfAbsent(path, PlainCommandResponse::new);
    }

    @Override
    public CommandResponse createRedirectResponse(String path) {
        return redirectResponseCache.computeIfAbsent(path, p -> new PlainCommandResponse(true, path));
    }

    private static class SimpleRequestFactoryHolder {
        private static final SimpleRequestFactory instance = new SimpleRequestFactory();
    }
}
