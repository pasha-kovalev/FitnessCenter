package com.epam.jwd.fitness_center.controller.filter;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandType;
import com.epam.jwd.fitness_center.controller.command.impl.CommandProvider;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebFilter(urlPatterns = "/*")
public class RoleFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(RoleFilter.class);
    private static final String COMMAND_PARAM_NAME = "command";
    private static final String USER_SESSION_ATTRIBUTE_NAME = "user";
    private final Map<UserRole, Set<Command>> commandsByRoles;


    public RoleFilter() {
        commandsByRoles = new EnumMap<>(UserRole.class);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        for (CommandType command : CommandType.values()) {
            for (UserRole allowedRole : command.getAllowedRoles()) {
                final Set<Command> commands = commandsByRoles.computeIfAbsent(allowedRole, k -> new HashSet<>());
                commands.add(CommandProvider.getInstance().of(command.name()));
            }
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final String commandName = req.getParameter(COMMAND_PARAM_NAME);
        if(commandName == null || hasPermissionForCommand(commandName, req)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendRedirect(PagePath.ERROR.getPath());
        }
    }

    private boolean hasPermissionForCommand(String commandName, HttpServletRequest request) {
        UserRole currentUserRole = retrieveCurrentUserRole(request);
        final Command command = Command.of(commandName);
        final Set<Command> allowedCommands = commandsByRoles.get(currentUserRole);
        return allowedCommands.contains(command);
    }

    private UserRole retrieveCurrentUserRole(HttpServletRequest request) {
        return Optional.ofNullable(request.getSession(false))
                .map(s -> (User) s.getAttribute(USER_SESSION_ATTRIBUTE_NAME))
                .map(User::getRole)
                .orElse(UserRole.GUEST);
    }
}
