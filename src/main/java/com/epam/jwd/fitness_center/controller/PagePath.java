package com.epam.jwd.fitness_center.controller;

/**The enum represents paths for redirect and forward routing types
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public enum PagePath {
    INDEX("/index.jsp"),
    CONTROLLER("/controller"),
    MAIN("/jsp/main.jsp"),
    MAIN_REDIRECT("controller?command=main_page"),
    LOGIN("/jsp/login.jsp"),
    LOGIN_REDIRECT("/controller?command=show_login"),
    SIGNUP("/jsp/signup.jsp"),
    SIGNUP_REDIRECT("/controller?command=show_signup"),
    SHOW_ERROR("/jsp/error/error.jsp"),
    SHOW_ERROR404("/jsp/error/error404.jsp"),
    SHOW_ERROR500("/jsp/error/error500.jsp"),
    ERROR("/controller?command=show_error"),
    ERROR404("/controller?command=show_error&value=404"),
    ERROR500("/controller?command=show_error&value=500"),
    MAIL_INFO("/jsp/info/send_mail_info.jsp"),
    MAIL_INFO_REDIRECT("/controller?command=show_mail_info"),
    EMAIL_CONFIRMED_INFO("/jsp/info/email_confirmed_info.jsp"),
    SHOW_INFO("/jsp/info/info.jsp"),
    SHOW_INFO_REDIRECT("/controller?command=show_info"),
    SHOW_ABOUT("/jsp/about.jsp"),
    SHOW_ABOUT_REDIRECT("/controller?command=show_about"),
    SHOW_PROGRAMS("/jsp/programs.jsp"),
    SHOW_PROGRAMS_REDIRECT("/controller?command=show_programs"),
    SHOW_TRANSFORM_PROGRAM("/jsp/transformation.jsp"),
    SHOW_TRANSFORM_PROGRAM_REDIRECT("/controller?command=SHOW_TRANSFORM_PROGRAM"),
    USER_CABINET("/jsp/user/cabinet.jsp"),
    TRAINER_CABINET("/jsp/trainer/cabinet.jsp"),
    ADMIN_CABINET("/jsp/admin/cabinet.jsp"),
    SHOW_CABINET_REDIRECT("/controller?command=show_cabinet"),
    SHOW_ORDER("/jsp/user/order.jsp"),
    SHOW_ORDER_REDIRECT("/controller?command=show_order"),
    SHOW_PAYMENT("/jsp/user/payment.jsp"),
    SHOW_PAYMENT_REDIRECT("/controller?command=show_payment"),
    SHOW_NEW_PROGRAM("/jsp/trainer/program.jsp"),
    SHOW_NEW_PROGRAM_REDIRECT("/controller?command=show_make_program"),
    SHOW_VIEW_EDIT_PROGRAM("/jsp/view_program.jsp"),
    SHOW_VIEW_EDIT_PROGRAM_REDIRECT("/controller?command=show_program");

    private final String path;

    PagePath(String path) {
        this.path = path;
    }

    /**Gets PagePath by its string representation
     * @param name name of enum or path value
     * @return page path
     */
    public static PagePath of(String name) {
        for (PagePath page : values()) {
            if (page.name().equalsIgnoreCase(name)) {
                return page;
            }
        }
        for (PagePath page : values()) {
            if (page.getPath().equalsIgnoreCase(name)) {
                return page;
            }
        }
        return MAIN_REDIRECT;
    }

    /**Checks if enum with given name exists
     * @param test name of enum
     * @return true if enum with given name exists
     */
    public static boolean contains(String test) {
        for (PagePath path : PagePath.values()) {
            if (path.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    /**Gets the path
     * @return path
     */
    public String getPath() {
        return path;
    }
}
