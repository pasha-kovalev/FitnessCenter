package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.RequestParameter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShowErrorCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowErrorCommand.class);
    private static final String ERROR_500_VALUE = "500";
    private static final String ERROR_404_VALUE = "404";
    private final ResponseCreator responseCreator;

    ShowErrorCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String value = request.getParameter(RequestParameter.VALUE);
        if (value == null) {
            return responseCreator.createForwardResponse(PagePath.SHOW_ERROR);
        }
        switch (value) {
            case ERROR_404_VALUE:
                return responseCreator.createForwardResponse(PagePath.SHOW_ERROR404);
            case ERROR_500_VALUE:
                return responseCreator.createForwardResponse(PagePath.SHOW_ERROR500);
            default:
                LOG.error("Error value {} not present", value);
                return responseCreator.createForwardResponse(PagePath.SHOW_ERROR);
        }
    }
}
