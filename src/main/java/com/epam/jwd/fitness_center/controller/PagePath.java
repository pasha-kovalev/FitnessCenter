package com.epam.jwd.fitness_center.controller;

public enum PagePath {
    INDEX("/controller?command=main_page"),
    CONTROLLER("/controller"),
    MAIN("/WEB-INF/jsp/main.jsp"),
    MAIN_REDIRECT("controller?command=main_page"),
    LOGIN("/WEB-INF/jsp/login.jsp"),
    LOGIN_REDIRECT("/controller?command=show_login"),
    SIGNUP("/WEB-INF/jsp/signup.jsp"),
    SIGNUP_REDIRECT("/controller?command=show_signup"),
    SHOW_ERROR("/WEB-INF/jsp/error/error.jsp"),
    SHOW_ERROR404("/WEB-INF/jsp/error/error404.jsp"),
    SHOW_ERROR500("/WEB-INF/jsp/error/error500.jsp"),
    ERROR("/controller?command=show_error"),
    ERROR404("/controller?command=show_error&value=404"),
    ERROR500("/controller?command=show_error&value=500"),
    MAIL_INFO("/WEB-INF/jsp/info/send_mail_info.jsp"),
    MAIL_INFO_REDIRECT("/controller?command=show_mail_info"),
    EMAIL_CONFIRMED_INFO("/WEB-INF/jsp/info/email_confirmed_info.jsp"),
    SHOW_INFO("/WEB-INF/jsp/info/info.jsp"),
    SHOW_INFO_REDIRECT("/controller?command=show_info"),
    SHOW_ABOUT("/WEB-INF/jsp/about.jsp"),
    SHOW_ABOUT_REDIRECT("/controller?command=show_about"),
    SHOW_PROGRAMS("/WEB-INF/jsp/programs.jsp"),
    SHOW_TRANSFORM_PROGRAM("/WEB-INF/jsp/transformation.jsp"),
    USER_CABINET("/WEB-INF/jsp/user/cabinet.jsp"),
    TRAINER_CABINET("/WEB-INF/jsp/trainer/cabinet.jsp"),
    ADMIN_CABINET("/WEB-INF/jsp/admin/cabinet.jsp"),
    SHOW_CABINET_REDIRECT("/controller?command=show_cabinet"),
    SHOW_ORDER("/WEB-INF/jsp/user/order.jsp"),
    SHOW_ORDER_REDIRECT("/controller?command=show_order"),
    SHOW_PAYMENT("/WEB-INF/jsp/user/payment.jsp"),
    SHOW_PAYMENT_REDIRECT("/controller?command=show_payment"),
    SHOW_NEW_PROGRAM("/WEB-INF/jsp/trainer/program.jsp"),
    SHOW_NEW_PROGRAM_REDIRECT("/controller?command=show_make_program"),
    SHOW_VIEW_EDIT_PROGRAM("/WEB-INF/jsp/view_program.jsp"),
    SHOW_VIEW_EDIT_PROGRAM_REDIRECT("/controller?command=show_program");

    private final String path;

    PagePath(String path) {
        this.path = path;
    }

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

    public static boolean contains(String test) {
        for (PagePath path : PagePath.values()) {
            if (path.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public String getPath() {
        return path;
    }
}
