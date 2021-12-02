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
    SHOW_INFO_REDIRECT("/controller?command=show_info");

    private final String path;

    PagePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static PagePath of(String name) {
        for (PagePath page : values()) {
            if (page.name().equalsIgnoreCase(name)) {
                return page;
            }
        }
        return MAIN_REDIRECT;
    }
}
