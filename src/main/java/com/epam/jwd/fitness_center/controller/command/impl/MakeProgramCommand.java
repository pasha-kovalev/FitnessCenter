package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.ProgramStatus;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class MakeProgramCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(MakeProgramCommand.class);
    private final ResponseCreator responseCreator;
    private final ProgramService programService;

    MakeProgramCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        programService = ServiceProvider.getInstance().getProgramService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        final String intensity = request.getParameter(RequestParameter.INTENSITY);
        final String schedule = request.getParameter(RequestParameter.SCHEDULE);
        final String exercises = request.getParameter(RequestParameter.EXERCISES);
        final String diet = request.getParameter(RequestParameter.DIET);
        final String equipment = request.getParameter(RequestParameter.EQUIPMENT);
        long orderId;
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (orderIdOptional.isPresent()) {
            orderId = orderIdOptional.get();
        } else {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        try {
            programService.insert(orderId, ProgramStatus.PENDING, intensity, schedule, exercises, diet, equipment);
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }
}
