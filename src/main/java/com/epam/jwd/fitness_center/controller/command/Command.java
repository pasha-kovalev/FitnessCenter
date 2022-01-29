package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.controller.command.impl.CommandProvider;

/** Interface of servlet command
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface Command {
    /** Retrieves command from CommandProvider by name
     * @param name string representation of command
     * @return command
     */
    static Command of(String name) {
        return CommandProvider.getInstance().of(name);
    }

    /** Executes command
     * @param request request wrapped with CommandRequest
     * @return CommandResponse containing the path and type of response
     */
    CommandResponse execute(CommandRequest request);
}
