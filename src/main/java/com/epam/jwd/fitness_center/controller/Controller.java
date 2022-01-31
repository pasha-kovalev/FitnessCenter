package com.epam.jwd.fitness_center.controller;

import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.model.connection.ConnectionPoolManager;
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

/** Represents a controller
 * @author Pavel Kovalev
 * @version 1.0
 */
@WebServlet("/controller")
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class Controller extends HttpServlet {
    private static final long serialVersionUID = -5223997271791449828L;
    private static final Logger LOG = LogManager.getLogger(Controller.class);

    public static final String COMMAND_NAME_PARAM = "command";

    private final RequestFactory requestFactory = RequestFactory.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        processRequest(req, resp);
    }

    /** Executes command from request parameter and then proceed with command response
     * @param req http request
     * @param resp http response
     */
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

    /** Writes ajax data to response or forwards request or sends redirect
     * @param req http request
     * @param resp http response
     * @param commandResponse executed response
     */
    private void proceedWithResponse(HttpServletRequest req, HttpServletResponse resp,
                                     CommandResponse commandResponse) {
        try {
            if (commandResponse.isAjax()) {
                processAjax(resp, commandResponse);
            } else {
                forwardOrRedirectToResponseLocation(req, resp, commandResponse);
            }
        } catch (ServletException e) {
            LOG.error("servlet exception occurred", e);
        } catch (IOException e) {
            LOG.error("IO exception occurred", e);
        }
    }

    /**Handles ajax response
     * @param resp http response
     * @param commandResponse command response with json data
     * @throws IOException when writing to http response exception occurs
     */
    private void processAjax(HttpServletResponse resp, CommandResponse commandResponse) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(commandResponse.getAjaxData());
    }

    /** Forwards request or sends redirect
     * @param req http request
     * @param resp http response
     * @param commandResponse command response
     * @throws IOException when send redirect exception occurs
     * @throws ServletException when forward exception occurs
     */
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

    @Override
    public void destroy() {
        super.destroy();
        ConnectionPoolManager.getInstance().shutDown();
    }
}
