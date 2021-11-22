package com.epam.jwd.fitness_center.controller;

public enum PagePath {
    INDEX("/"),
    MAIN("/WEB-INF/jsp/main.jsp"),
    USERS("/WEB-INF/jsp/users.jsp"),
    LOGIN("/WEB-INF/jsp/login.jsp"),
    ERROR("/WEB-INF/jsp/error.jsp");

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
        return MAIN;
    }
}
