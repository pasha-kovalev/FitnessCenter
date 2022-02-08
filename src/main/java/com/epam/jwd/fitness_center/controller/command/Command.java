package com.epam.jwd.fitness_center.controller.command;

/**
 * Interface of servlet command
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface Command {
    /**
     * Executes command
     *
     * @param request request wrapped with CommandRequest
     * @return CommandResponse containing the path and type of response
     */
    CommandResponse execute(CommandRequest request);
}
