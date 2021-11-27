package com.epam.jwd.fitness_center.command.impl;

import com.epam.jwd.fitness_center.command.Command;
import com.epam.jwd.fitness_center.command.CommandRequest;
import com.epam.jwd.fitness_center.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.PagePath;

public class ShowMainPageCommand implements Command {
    public static final CommandResponse FORWARD_TO_MAIN_PAGE_RESPONSE = new CommandResponse() {
        @Override
        public boolean isRedirect() {
            return false;
        }

        @Override
        public String getPath() {
            return PagePath.MAIN.getPath();
        }
    };

    @Override
    public CommandResponse execute(CommandRequest request) {
        return FORWARD_TO_MAIN_PAGE_RESPONSE;
    }


}
