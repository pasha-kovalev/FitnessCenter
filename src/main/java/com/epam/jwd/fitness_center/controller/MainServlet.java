package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.command.Command;
import com.epam.jwd.fitness_center.command.CommandResponse;
import com.epam.jwd.fitness_center.db.ConnectionPoolManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class MainServlet extends HttpServlet {
    private static final long serialVersionUID = -5223997271791449828L;
    private static final Logger LOG = LogManager.getLogger(MainServlet.class);

    @Override
    public void init() {
        ConnectionPoolManager.getInstance().init();
    }

    @Override
    public void destroy() {
        ConnectionPoolManager.getInstance().shutDown();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        LOG.trace("req and resp in doGet");
        final String commandName = req.getParameter("command");
        final Command command = Command.of(commandName);
        final CommandResponse commandResponse = command.execute(req::setAttribute);
        proceedWithResponse(req, resp, commandResponse);


    }

    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp,
                                     CommandResponse commandResponse) {
        try {
            forwardOrRedirectToResponseLocation(req, resp, commandResponse);
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
