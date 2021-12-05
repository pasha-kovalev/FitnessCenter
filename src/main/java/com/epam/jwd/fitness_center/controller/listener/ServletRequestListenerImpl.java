package com.epam.jwd.fitness_center.controller.listener;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.command.CommandType;
import com.epam.jwd.fitness_center.controller.command.RequestParameter;
import com.epam.jwd.fitness_center.controller.command.SessionAttribute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@WebListener
public class ServletRequestListenerImpl implements ServletRequestListener {
    private static final Logger LOG = LogManager.getLogger(ServletRequestListenerImpl.class);
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest servletRequest = (HttpServletRequest) sre.getServletRequest();
        setEncoding(servletRequest);
        savePreviousQuery(servletRequest);
    }

    private void savePreviousQuery(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(true);
        String command = servletRequest.getParameter(RequestParameter.COMMAND);
        if(command != null) {
            if(command.compareToIgnoreCase(CommandType.SWITCH_LOCALE.name()) != 0) {
                String query = servletRequest.getQueryString();
                session.setAttribute(SessionAttribute.PREVIOUS_QUERY, query);
            }
        }
    }

    private void setEncoding(HttpServletRequest servletRequest) {
        try {
            servletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Incorrect encoding", e);
        }
    }
}
