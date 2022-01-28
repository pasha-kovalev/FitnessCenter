package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.util.Arrays;
import java.util.List;

import static com.epam.jwd.fitness_center.model.entity.UserRole.*;

/**The enum represents command type and roles allowed to execute that command
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public enum CommandType {
    DEFAULT,
    MAIN_PAGE,
    LOGIN(GUEST),
    SHOW_USERS(ADMIN),
    SHOW_LOGIN(GUEST),
    SHOW_ERROR,
    LOG_OUT(USER, TRAINER, ADMIN, REGULAR_USER, CORPORATE_USER),
    SHOW_SIGNUP(GUEST),
    SIGNUP(GUEST),
    SHOW_MAIL_INFO(GUEST),
    RESEND_EMAIL(GUEST),
    CONFIRM_EMAIL(GUEST),
    SHOW_CONFIRM_EMAIL(GUEST),
    SWITCH_LOCALE,
    SHOW_INFO,
    SHOW_ABOUT,
    SHOW_PROGRAMS,
    SHOW_TRANSFORM_PROGRAM,
    SHOW_CABINET,
    SHOW_ORDER(USER, CORPORATE_USER, REGULAR_USER),
    ORDER(USER, CORPORATE_USER, REGULAR_USER),
    SHOW_PAYMENT(USER, CORPORATE_USER, REGULAR_USER),
    PAYMENT(USER, CORPORATE_USER, REGULAR_USER),
    SHOW_USER_ORDERS(USER, CORPORATE_USER, REGULAR_USER),
    SHOW_TRAINER_ORDERS(TRAINER),
    SHOW_UNTAKEN_ORDERS(TRAINER),
    SHOW_MAKE_PROGRAM(TRAINER),
    MAKE_PROGRAM(TRAINER),
    SHOW_PROGRAM(USER, CORPORATE_USER, REGULAR_USER, TRAINER),
    UPDATE_PROGRAM(USER, CORPORATE_USER, REGULAR_USER, TRAINER),
    SHOW_PROGRAM_DETAILS(USER, CORPORATE_USER, REGULAR_USER, TRAINER),
    SEND_REVIEW(USER, CORPORATE_USER, REGULAR_USER),
    UPLOAD_IMAGE(TRAINER),
    EDIT_USER_DATA(TRAINER),
    CANCEL_ORDER(USER, CORPORATE_USER, REGULAR_USER, TRAINER),
    SHOW_MANAGE_USERS(ADMIN),
    MANAGE_USER_DATA(ADMIN),
    SHOW_MANAGE_ITEMS(ADMIN),
    MANAGE_ITEM_DATA(ADMIN),
    MANAGE_NEW_ITEM_DATA(ADMIN),
    DELETE_ITEM(ADMIN),
    SHOW_MANAGE_DISCOUNT(ADMIN),
    MANAGE_NEW_DISCOUNT(ADMIN);

    private final List<UserRole> allowedRoles;

    CommandType(UserRole... roles) {
        this.allowedRoles = roles != null && roles.length > 0 ? Arrays.asList(roles) : UserRole.valuesAsList();
    }

    /**Gets command type by its string representation
     * @param name name of command type
     * @return command type
     */
    public static CommandType of(String name) {
        for (CommandType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return DEFAULT;
    }

    /**Gets allowed roles for a command
     * @return List of allowed roles
     */
    public List<UserRole> getAllowedRoles() {
        return allowedRoles;
    }
}
