package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.PlainCommandResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleResponseCreator implements ResponseCreator {
    private final Map<String, CommandResponse> forwardResponseCache = new ConcurrentHashMap<>();
    private final Map<String, CommandResponse> redirectResponseCache = new ConcurrentHashMap<>();

    private SimpleResponseCreator() {
    }

    public static SimpleResponseCreator getInstance() {
        return SimpleRequestFactoryHolder.instance;
    }

    @Override
    public CommandResponse createForwardResponse(PagePath page) {
        return forwardResponseCache.computeIfAbsent(page.getPath(), PlainCommandResponse::new);
    }

    @Override
    public CommandResponse createRedirectResponse(PagePath page) {
        return redirectResponseCache.computeIfAbsent(page.getPath(), p ->
                new PlainCommandResponse(true, page.getPath()));
    }

    @Override
    public CommandResponse createAjaxResponse(String data) {
        return PlainCommandResponse.createAjaxResponse(data);
    }

    public CommandResponse createRedirectResponse(String path) {
        return redirectResponseCache.computeIfAbsent(path, p ->
                new PlainCommandResponse(true, path));
    }


    private static class SimpleRequestFactoryHolder {
        private static final SimpleResponseCreator instance = new SimpleResponseCreator();
    }
}
