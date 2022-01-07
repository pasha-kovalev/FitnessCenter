package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.controller.command.impl.CommandProvider;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public interface Command {
    Logger LOG = LogManager.getLogger(Command.class);

    static Command of(String name) {
        return CommandProvider.getInstance().of(name);
    }

    CommandResponse execute(CommandRequest request);
    
    default Optional<UserDetails> retrieveUserDetailsFromSession(CommandRequest request) {
        Optional<Object> userDetailsOptional = request.retrieveFromSession(SessionAttribute.USER_DETAILS);
        if (!userDetailsOptional.isPresent()) {
            LOG.error("Not found user details for user: {}",
                    request.retrieveFromSession(SessionAttribute.USER).isPresent()
                            ? request.retrieveFromSession(SessionAttribute.USER).get() : "user not found in session");
            return Optional.empty();
        }
        return Optional.of((UserDetails) userDetailsOptional.get());
    }
}
