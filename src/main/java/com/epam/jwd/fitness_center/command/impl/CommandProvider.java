package com.epam.jwd.fitness_center.command.impl;

import com.epam.jwd.fitness_center.command.Command;
import com.epam.jwd.fitness_center.command.CommandType;
import com.epam.jwd.fitness_center.controller.PropertyContext;
import com.epam.jwd.fitness_center.controller.RequestFactory;

import java.util.EnumMap;

import static com.epam.jwd.fitness_center.command.CommandType.*;

public class CommandProvider {
    private final EnumMap<CommandType, Command> commands = new EnumMap(CommandType.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();
    private final PropertyContext propertyContext = PropertyContext.getInstance();

    private CommandProvider() {
        commands.put(DEFAULT, new ShowMainPageCommand());
        commands.put(MAIN_PAGE, commands.get(DEFAULT));
        commands.put(SHOW_USERS, new ShowUsersPageCommand());
        commands.put(LOGIN, new LoginCommand(requestFactory, propertyContext));
        commands.put(SHOW_LOGIN, new ShowLoginPageCommand(requestFactory, propertyContext));
    }
    public Command of(String name) {
        return commands.get(CommandType.of(name));
    }


    public static CommandProvider getInstance() {
        return CommandProviderHolder.INSTANCE;
    }

    private static class CommandProviderHolder {
        private static final CommandProvider INSTANCE = new CommandProvider();
    }
}
