package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.SimpleResponseCreator;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandType;

import java.util.EnumMap;

import static com.epam.jwd.fitness_center.controller.command.CommandType.*;

public class CommandProvider {
    private final EnumMap<CommandType, Command> commands = new EnumMap(CommandType.class);
    private final ResponseCreator responseCreator = ResponseCreator.getCreator();

    private CommandProvider() {
        commands.put(DEFAULT, new DefaultCommand(responseCreator));
        commands.put(MAIN_PAGE, new ShowMainPageCommand());
        commands.put(LOGIN, new LoginCommand(responseCreator));
        commands.put(SHOW_LOGIN, new ShowLoginPageCommand(responseCreator));
        commands.put(LOG_OUT, new LogOutCommand(responseCreator));
        commands.put(SHOW_SIGNUP, new ShowSignupCommand(responseCreator));
        commands.put(SIGNUP, new SignupCommand(responseCreator));
        commands.put(SHOW_ERROR, new ShowErrorCommand(responseCreator));
        commands.put(SHOW_MAIL_INFO, new ShowSendMailInfoCommand(responseCreator));
        commands.put(RESEND_EMAIL, new ResendEmailConfirmationCommand(responseCreator));
        commands.put(CONFIRM_EMAIL, new ConfirmEmailCommand(responseCreator));
        commands.put(SHOW_CONFIRM_EMAIL, new ShowEmailConfirmationPageCommand(responseCreator));
        commands.put(SWITCH_LOCALE, new SwitchLocaleCommand((SimpleResponseCreator) responseCreator));
        commands.put(SHOW_INFO, new ShowInfoCommand(responseCreator));
        commands.put(SHOW_ABOUT, new ShowAboutPageCommand(responseCreator));
        commands.put(SHOW_PROGRAMS, new ShowProgramsCommand(responseCreator));
        commands.put(SHOW_TRANSFORM_PROGRAM, new ShowTransformProgramCommand(responseCreator));
        commands.put(SHOW_CABINET, new ShowCabinetCommand(responseCreator));
        commands.put(SHOW_ORDER, new ShowOrderPageCommand(responseCreator));
        commands.put(ORDER, new MakeOrderCommand(responseCreator));
        commands.put(SHOW_PAYMENT, new ShowPaymentPageCommand(responseCreator));
        commands.put(PAYMENT, new PaymentCommand(responseCreator));
        commands.put(SHOW_USER_ORDERS, new ShowUserOrdersPageCommand(responseCreator));
        commands.put(SHOW_TRAINER_ORDERS, new ShowTrainerOrdersPageCommand(responseCreator));
        commands.put(SHOW_MAKE_PROGRAM, new ShowMakeProgramPageCommand(responseCreator));
        commands.put(MAKE_PROGRAM, new MakeProgramCommand(responseCreator));
        commands.put(SHOW_PROGRAM, new ShowProgramPageCommand(responseCreator));
        commands.put(UPDATE_PROGRAM, new UpdateProgramCommand(responseCreator));
        commands.put(SHOW_PROGRAM_DETAILS, new ShowProgramDetails(responseCreator));
        commands.put(SEND_REVIEW, new SendOrderReviewCommand(responseCreator));
        commands.put(UPLOAD_IMAGE, new UploadImageCommand(responseCreator));
        commands.put(EDIT_USER_DATA, new EditUserDataCommand(responseCreator));
        commands.put(CANCEL_ORDER, new CancelOrderCommand(responseCreator));
        commands.put(SHOW_MANAGE_USERS, new ShowManageUsersPageCommand(responseCreator));
        commands.put(MANAGE_USER_DATA, new ManageUserDataCommand(responseCreator));
        commands.put(SHOW_MANAGE_ITEMS, new ShowManageItemsPageCommand(responseCreator));
        commands.put(MANAGE_ITEM_DATA, new ManageItemDataCommand(responseCreator));
        commands.put(MANAGE_NEW_ITEM_DATA, new ManageNewItemDataCommand(responseCreator));
        commands.put(DELETE_ITEM, new DeleteItemCommand(responseCreator));
        commands.put(SHOW_MANAGE_DISCOUNT, new ShowManageDiscountPageCommand(responseCreator));
        commands.put(MANAGE_NEW_DISCOUNT, new ManageNewDiscountCommand(responseCreator));
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

