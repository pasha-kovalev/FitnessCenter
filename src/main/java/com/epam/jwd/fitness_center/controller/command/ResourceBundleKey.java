package com.epam.jwd.fitness_center.controller.command;

/**The class provides string constant represents keys in resource bundle
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public class ResourceBundleKey {
    public static final String INFO_EMAIL_RESEND = "info.email.resend";
    public static final String INFO_ERROR_USER_CONFIRMED = "info.email.error.userConfirmed";
    public static final String INFO_ERROR_USER_NOT_FOUND = "info.email.error.userNotFound";
    public static final String INFO_ERROR_LINK = "info.email.error.link";
    public static final String INFO_VALID_LINK = "emailInfo.msg.success";
    public static final String LOGIN_ERROR = "login.exception";
    public static final String BANNED_ERROR = "banned.exception";
    public static final String SIGNUP_ERROR = "signup.error";
    public static final String INFO_SUCCESS = "payment.msg.success";
    public static final String INFO_PAYMENT_CARD_NOT_EXIST = "payment.err.cardNotExist";
    public static final String INFO_PAYMENT_INSUFFICIENT_FUNDS = "payment.err.funds";

    private ResourceBundleKey() {
    }
}
