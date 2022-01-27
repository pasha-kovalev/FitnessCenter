package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.SimpleRequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandType;

import java.util.EnumMap;

import static com.epam.jwd.fitness_center.controller.command.CommandType.*;

public class CommandProvider {
    private final EnumMap<CommandType, Command> commands = new EnumMap(CommandType.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    private CommandProvider() {
        commands.put(DEFAULT, new DefaultCommand(requestFactory));
        commands.put(MAIN_PAGE, new ShowMainPageCommand());
        commands.put(SHOW_USERS, new ShowUsersPageCommand());
        commands.put(LOGIN, new LoginCommand(requestFactory));
        commands.put(SHOW_LOGIN, new ShowLoginPageCommand(requestFactory));
        commands.put(LOG_OUT, new LogOutCommand(requestFactory));
        commands.put(SHOW_SIGNUP, new ShowSignupCommand(requestFactory));
        commands.put(SIGNUP, new SignupCommand(requestFactory));
        commands.put(SHOW_ERROR, new ShowErrorCommand(requestFactory));
        commands.put(SHOW_MAIL_INFO, new ShowSendMailInfoCommand(requestFactory));
        commands.put(RESEND_EMAIL, new ResendEmailConfirmationCommand(requestFactory));
        commands.put(CONFIRM_EMAIL, new ConfirmEmailCommand(requestFactory));
        commands.put(SHOW_CONFIRM_EMAIL, new ShowEmailConfirmationPageCommand(requestFactory));
        commands.put(SWITCH_LOCALE, new SwitchLocaleCommand((SimpleRequestFactory) requestFactory));
        commands.put(SHOW_INFO, new ShowInfoCommand(requestFactory));
        commands.put(SHOW_ABOUT, new ShowAboutPageCommand(requestFactory));
        commands.put(SHOW_PROGRAMS, new ShowProgramsCommand(requestFactory));
        commands.put(SHOW_TRANSFORM_PROGRAM, new ShowTransformProgramCommand(requestFactory));
        commands.put(SHOW_CABINET, new ShowCabinetCommand(requestFactory));
        commands.put(SHOW_ORDER, new ShowOrderPageCommand(requestFactory));
        commands.put(ORDER, new MakeOrderCommand(requestFactory));
        commands.put(SHOW_PAYMENT, new ShowPaymentPageCommand(requestFactory));
        commands.put(PAYMENT, new PaymentCommand(requestFactory));
        commands.put(SHOW_USER_ORDERS, new ShowUserOrdersPageCommand(requestFactory));
        commands.put(SHOW_TRAINER_ORDERS, new ShowTrainerOrdersPageCommand(requestFactory));
        commands.put(SHOW_MAKE_PROGRAM, new ShowMakeProgramPageCommand(requestFactory));
        commands.put(MAKE_PROGRAM, new MakeProgramCommand(requestFactory));
        commands.put(SHOW_PROGRAM, new ShowProgramPageCommand(requestFactory));
        commands.put(UPDATE_PROGRAM, new UpdateProgramCommand(requestFactory));
        commands.put(SHOW_PROGRAM_DETAILS, new ShowProgramDetails(requestFactory));
        commands.put(SEND_REVIEW, new SendOrderReviewCommand(requestFactory));
        commands.put(UPLOAD_IMAGE, new UploadImageCommand(requestFactory));
        commands.put(EDIT_USER_DATA, new EditUserDataCommand(requestFactory));
        commands.put(CANCEL_ORDER, new CancelOrderCommand(requestFactory));
        commands.put(SHOW_MANAGE_USERS, new ShowManageUsersPageCommand(requestFactory));
        commands.put(MANAGE_USER_DATA, new ManageUserDataCommand(requestFactory));
        commands.put(SHOW_MANAGE_ITEMS, new ShowManageItemsPageCommand(requestFactory));
        commands.put(MANAGE_ITEM_DATA, new ManageItemDataCommand(requestFactory));
        commands.put(MANAGE_NEW_ITEM_DATA, new ManageNewItemDataCommand(requestFactory));
        commands.put(DELETE_ITEM, new DeleteItemCommand(requestFactory));

    }

    public static CommandProvider getInstance() {
        return CommandProviderHolder.INSTANCE;
    }

    public Command of(String name) {
        return commands.get(CommandType.of(name));
    }

    private static class CommandProviderHolder {
        private static final CommandProvider INSTANCE = new CommandProvider();
    }
}

