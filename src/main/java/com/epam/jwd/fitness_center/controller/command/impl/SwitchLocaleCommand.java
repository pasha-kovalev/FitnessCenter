package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.SimpleRequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SwitchLocaleCommand implements Command {

    public static final String QUESTION_MARK = "?";
    private final SimpleRequestFactory requestFactory;
    //todo improve
    private final List<String> locales = new ArrayList<>(Arrays.asList("en", "ru"));

    SwitchLocaleCommand(SimpleRequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String locale = request.getParameter(RequestParameter.LOCALE);
        if (locale != null && locales.contains(locale)) {
            request.addToSession(RequestParameter.LOCALE, locale);
        }
        Optional<Object> optionalPage = request.retrieveFromSession(Attribute.CURRENT_PAGE);
        if (optionalPage.isPresent()) {
            String page = (String) optionalPage.get();
            if(page.contains(PagePath.CONTROLLER.getPath())) {
                return requestFactory.createRedirectResponse(page);
            } else {
                String pagePathRedirectStr = PagePath.of(page).name() + "_REDIRECT";
                if(PagePath.contains(pagePathRedirectStr)) {
                    return requestFactory.createRedirectResponse(PagePath.of(pagePathRedirectStr));
                }
                return requestFactory.createForwardResponse(PagePath.of(page));
            }
        }
        return requestFactory.createRedirectResponse(PagePath.INDEX);
    }
}
