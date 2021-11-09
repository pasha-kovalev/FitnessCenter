package com.epam.jwd.fitness_center.command;

import com.epam.jwd.fitness_center.app.Application;

public enum ShowNotFoundPageCommand implements Command {
    INSTANCE;

    public static final CommandResponse FORWARD_TO_MAIN_PAGE_RESPONSE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return "/WEB-INF/jsp/not_found.jsp";
        }
    };
    @Override
    public CommandResponse execute(CommandRequest request) {
        return FORWARD_TO_MAIN_PAGE_RESPONSE;
    }
}
