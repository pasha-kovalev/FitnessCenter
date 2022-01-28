package com.epam.jwd.fitness_center.controller.command;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.impl.CommandProvider;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.validator.NumberValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

/** Interface of servlet command
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface Command {
    Logger LOG = LogManager.getLogger(Command.class);

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

    /** Creates redirect response to info page with error message
     * @param requestFactory request factory
     * @param request request wrapped with CommandRequest
     * @return CommandResponse containing the path and type of response
     */
    default CommandResponse createInfoErrorResponse(RequestFactory requestFactory, CommandRequest request) {
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_ERROR_LINK);
        return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }

    /** Retrieves UserDetails from session
     * @param request request wrapped with CommandRequest
     * @return optional of UserDetails or Optional.empty() if not present in session
     */
    default Optional<UserDetails> retrieveUserDetailsFromSession(CommandRequest request) {
        Optional<Object> userDetailsOptional = request.retrieveFromSession(Attribute.USER_DETAILS);
        if (!userDetailsOptional.isPresent()) {
            LOG.error("Not found user details for user: {}",
                    request.retrieveFromSession(Attribute.USER).isPresent()
                            ? request.retrieveFromSession(Attribute.USER).get() : "user not found in session");
            return Optional.empty();
        }
        return Optional.of((UserDetails) userDetailsOptional.get());
    }

    /** Retrieves User from session
     * @param request request wrapped with CommandRequest
     * @return optional of User or Optional.empty() if not present in session
     */
    default Optional<User> retrieveUserFromSession(CommandRequest request) {
        Optional<Object> userOptional = request.retrieveFromSession(Attribute.USER);
        if (!userOptional.isPresent()) {
            LOG.error("Not found user: {}",
                    request.retrieveFromSession(Attribute.USER).isPresent()
                            ? request.retrieveFromSession(Attribute.USER).get() : "user not found in session");
            return Optional.empty();
        }
        return Optional.of((User) userOptional.get());
    }

    /** Retrieves and checks positive long from session
     * @param request request wrapped with CommandRequest
     * @param parameter parameter of value
     * @return optional of Long or Optional.empty() if not present in session
     */
    default Optional<Long> retrievePositiveLongParameter(CommandRequest request, String parameter) {
        String paramStr = request.getParameter(parameter);
        if (paramStr != null) {
            if (!NumberValidator.isPositiveInteger(paramStr)) {
                return Optional.empty();
            }
            long number = Long.parseLong(paramStr);
            return Optional.of(number);
        }
        return Optional.empty();
    }
}
