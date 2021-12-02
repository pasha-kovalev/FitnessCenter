package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.SimpleRequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        if(locale != null && locales.contains(locale)) {
            request.addToSession(SessionAttribute.LOCALE, locale);
        }
        Optional<Object> optionalQuery = request.retrieveFromSession(SessionAttribute.PREVIOUS_QUERY);
        if(optionalQuery.isPresent()) {
            String previousQuery = (String) optionalQuery.get();
            String path = previousQuery.isEmpty() ? PagePath.MAIN_REDIRECT.getPath() : PagePath.CONTROLLER.getPath()
                    + QUESTION_MARK + previousQuery;
            return requestFactory.createRedirectResponse(path);
        }
        return requestFactory.createRedirectResponse(PagePath.INDEX);
    }
}
