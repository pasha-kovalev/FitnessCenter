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
        Optional<Object> optionalQuery = request.retrieveFromSession(Attribute.PREVIOUS_QUERY);
        if (optionalQuery.isPresent()) {
            String previousQuery = (String) optionalQuery.get();
            String path = previousQuery.isEmpty() ? PagePath.MAIN_REDIRECT.getPath() : PagePath.CONTROLLER.getPath()
                    + QUESTION_MARK + previousQuery;
            return requestFactory.createRedirectResponse(path);
        }
        return requestFactory.createRedirectResponse(PagePath.INDEX);
    }
}
