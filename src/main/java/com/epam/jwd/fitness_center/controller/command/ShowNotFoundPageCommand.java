package com.epam.jwd.fitness_center.controller.command;

public enum ShowNotFoundPageCommand implements Command {
    INSTANCE;

    public static final CommandResponse FORWARD_TO_NOT_FOUND_PAGE_RESPONSE = new CommandResponse() {
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
        return FORWARD_TO_NOT_FOUND_PAGE_RESPONSE;
    }
}
