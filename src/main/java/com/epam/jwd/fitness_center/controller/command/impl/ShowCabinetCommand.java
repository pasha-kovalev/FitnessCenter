package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.util.Optional;

public class ShowCabinetCommand implements Command {
    private final RequestFactory requestFactory;

    ShowCabinetCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        Optional<User> userOptional = retrieveUserFromSession(request);
        if (!userOptional.isPresent()) return requestFactory.createForwardResponse(PagePath.ERROR);
        User user = userOptional.get();
        UserRole userRole = user.getRole();
        switch (userRole) {
            case USER:
            case REGULAR_USER:
            case CORPORATE_USER:
                return requestFactory.createForwardResponse(PagePath.USER_CABINET);
            case TRAINER:
                return requestFactory.createForwardResponse(PagePath.TRAINER_CABINET);
            case ADMIN:
                return requestFactory.createForwardResponse(PagePath.ADMIN_CABINET);
            default:
                return requestFactory.createForwardResponse(PagePath.ERROR);
        }
    }
}
