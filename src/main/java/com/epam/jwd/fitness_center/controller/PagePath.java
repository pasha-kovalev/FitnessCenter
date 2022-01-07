package com.epam.jwd.fitness_center.controller;

public enum PagePath {
    INDEX("/controller?command=main_page"),
    CONTROLLER("/controller"),
    MAIN("/WEB-INF/jsp/main.jsp"),
    MAIN_REDIRECT("controller?command=main_page"),
    USERS("/WEB-INF/jsp/users.jsp"),
    LOGIN("/WEB-INF/jsp/login.jsp"),
    LOGIN_REDIRECT("/controller?command=show_login"),
    SIGNUP("/WEB-INF/jsp/signup.jsp"),
    SIGNUP_REDIRECT("/controller?command=show_signup"),
    ERROR("/controller?command=show_error"),
    SHOW_ERROR("/WEB-INF/jsp/error.jsp"),
    ERROR404("/WEB-INF/jsp/error404.jsp"),
    ERROR500("/WEB-INF/jsp/error500.jsp"),
    MAIL_INFO("/WEB-INF/jsp/send_mail_info.jsp"),
    MAIL_INFO_REDIRECT("/controller?command=show_mail_info"),
    EMAIL_CONFIRMED_INFO("/WEB-INF/jsp/email_confirmed_info.jsp"),
    SHOW_INFO("/WEB-INF/jsp/info.jsp"),
    SHOW_INFO_REDIRECT("/controller?command=show_info"),
    SHOW_ABOUT("/WEB-INF/jsp/about.jsp"),
    SHOW_PROGRAMS("/WEB-INF/jsp/programs.jsp"),
    SHOW_TRANSFORM_PROGRAM("/WEB-INF/jsp/transformation.jsp"),
    USER_CABINET("/WEB-INF/jsp/user/cabinet.jsp"),
    SHOW_ORDER("/WEB-INF/jsp/user/order.jsp"),
    SHOW_ORDER_REDIRECT("/controller?command=show_order"),
    SHOW_PAYMENT("/WEB-INF/jsp/user/payment.jsp"),
    SHOW_PAYMENT_REDIRECT("/controller?command=show_payment");

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
        return MAIN_REDIRECT;
    }

    public String getPath() {
        return path;
    }
}
