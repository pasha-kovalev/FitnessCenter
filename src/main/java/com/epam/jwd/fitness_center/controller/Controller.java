package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {
    public static final String COMMAND_NAME_PARAM = "command";
    private static final long serialVersionUID = -5223997271791449828L;
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        LOG.trace("req and resp in doGet");
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        LOG.trace("req and resp in doPost");
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) {
        final String commandName = req.getParameter(COMMAND_NAME_PARAM);
        final Command command = Command.of(commandName);
        final CommandRequest commandRequest = requestFactory.createRequest(req);
        final CommandResponse commandResponse = command.execute(commandRequest);
        if (commandResponse.getPath() != null &&
                !commandResponse.getPath().contains(CommandType.SWITCH_LOCALE.name().toLowerCase())) {
            req.getSession().setAttribute(Attribute.CURRENT_PAGE, commandResponse.getPath());
        }
        proceedWithResponse(req, resp, commandResponse);
    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp,
                                     CommandResponse commandResponse) {
        try {
            if (commandResponse.isAjax()) {
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");
                resp.getWriter().write(commandResponse.getAjaxData());
            } else {
                forwardOrRedirectToResponseLocation(req, resp, commandResponse);
            }
        } catch (ServletException e) {
            LOG.error("servlet exception occurred", e);
        } catch (IOException e) {
            LOG.error("IO exception occurred", e);
        }
    }

    private void forwardOrRedirectToResponseLocation(HttpServletRequest req, HttpServletResponse resp,
                                                     CommandResponse commandResponse) throws IOException, ServletException {
        if (commandResponse.isRedirect()) {
            resp.sendRedirect(commandResponse.getPath());
        } else {
            final String desiredPath = commandResponse.getPath();
            final RequestDispatcher dispatcher = req.getRequestDispatcher(desiredPath);
            dispatcher.forward(req, resp);
        }
    }
}
