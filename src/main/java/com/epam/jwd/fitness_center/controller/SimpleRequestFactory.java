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

    /*//create refresh
    @Override
    public CommandResponse createRedirectResponse(PagePath page) {
        return redirectResponseCache.computeIfAbsent(page.getPath(), p ->
                new PlainCommandResponse(true, page.getPath()));
    }*/

    private static class SimpleRequestFactoryHolder {
        private static final SimpleRequestFactory instance = new SimpleRequestFactory();
    }
}
