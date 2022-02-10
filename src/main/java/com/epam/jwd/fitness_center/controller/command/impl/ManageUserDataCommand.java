package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.controller.listener.HttpSessionListenerImpl;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.Optional;

public class ManageUserDataCommand implements Command {
    public static final String STATUS_FIELD_NAME = "status";
    public static final String ROLE_FIELD_NAME = "role";
    private static final Logger LOG = LogManager.getLogger(ManageUserDataCommand.class);
    private final ResponseCreator responseCreator;
    private final UserService userService;

    ManageUserDataCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String[] fieldNames = request.getParameterValues(RequestParameter.NAME);
        String[] values = request.getParameterValues(RequestParameter.VALUE);
        Optional<Long> userIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.USER_ID);
        if (!userIdOptional.isPresent() || fieldNames == null || values == null || fieldNames.length != values.length) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        long userId = userIdOptional.get();
        try {
            for (int i = 0; i < fieldNames.length; i++) {
                switch (fieldNames[i]) {
                    case STATUS_FIELD_NAME:
                        UserStatus status = UserStatus.valueOf(values[i].toUpperCase());
                        userService.updateUserStatus(status, userId);
                        if (status == UserStatus.BANNED) {
                            invalidateUserSession(request, userId);
                        }
                        break;
                    case ROLE_FIELD_NAME:
                        userService.updateUserRole(UserRole.valueOf(values[i].toUpperCase()), userId);
                        break;
                    default:
                        LOG.error("Not found field name: {}", fieldNames[i]);
                        return responseCreator.createRedirectResponse(PagePath.ERROR);
                }
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createAjaxResponse(json);
    }

    private void invalidateUserSession(CommandRequest request, long userId) {
        Map<Long, HttpSession> sessionMap = HttpSessionListenerImpl.getSessionMap(request.getServletContext());
        HttpSession httpSession = sessionMap.get(userId);
        if (httpSession != null) {
            httpSession.invalidate();
        }
    }
}