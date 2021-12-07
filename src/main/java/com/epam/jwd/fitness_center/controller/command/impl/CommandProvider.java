package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.SimpleRequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandType;
import com.epam.jwd.fitness_center.controller.RequestFactory;

import java.util.EnumMap;

import static com.epam.jwd.fitness_center.controller.command.CommandType.*;

public class CommandProvider {
    private final EnumMap<CommandType, Command> commands = new EnumMap(CommandType.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CommandProvider() {
        commands.put(DEFAULT, new ShowMainPageCommand());
        commands.put(MAIN_PAGE, commands.get(DEFAULT));
        commands.put(SHOW_USERS, new ShowUsersPageCommand());
        commands.put(LOGIN, new LoginCommand(requestFactory));
        commands.put(SHOW_LOGIN, new ShowLoginPageCommand(requestFactory));
        commands.put(LOG_OUT, new LogOutCommand(requestFactory));
        commands.put(SHOW_SIGNUP, new ShowSignupCommand(requestFactory));
        commands.put(SIGNUP, new SignupCommand(requestFactory));
        commands.put(SHOW_ERROR, new ShowErrorCommand(requestFactory));
        commands.put(SHOW_MAIL_INFO, new ShowSendMailInfoCommand(requestFactory));
        commands.put(RESEND_EMAIL, new ResendEmailConfirmationCommand(requestFactory));
        commands.put(CONFIRM_EMAIL , new ConfirmEmailCommand(requestFactory));
        commands.put(SHOW_CONFIRM_EMAIL, new ShowEmailConfirmationPageCommand(requestFactory));
        commands.put(SWITCH_LOCALE, new SwitchLocaleCommand((SimpleRequestFactory) requestFactory));
        commands.put(SHOW_INFO, new ShowInfoCommand(requestFactory));
        commands.put(SHOW_ABOUT, new ShowAboutCommand(requestFactory));
        commands.put(SHOW_PROGRAMS, new ShowProgramsCommand(requestFactory));
        commands.put(SHOW_TRANSFORM_PROGRAM, new ShowTransformProgramCommand(requestFactory));
        commands.put(USER_CABINET, new ShowUserCabinetCommand(requestFactory));
        commands.put(SHOW_ORDER, new ShowOrderPageCommand(requestFactory));
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

