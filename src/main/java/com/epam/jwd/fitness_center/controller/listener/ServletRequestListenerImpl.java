package com.epam.jwd.fitness_center.controller.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@WebListener
public class ServletRequestListenerImpl implements ServletRequestListener {
    private static final Logger LOG = LogManager.getLogger(ServletRequestListenerImpl.class);

    /**
     * Sets character encoding
     *
     * @param sre servlet request initialization event
     */
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest servletRequest = (HttpServletRequest) sre.getServletRequest();
        setEncoding(servletRequest);
    }

    private void setEncoding(HttpServletRequest servletRequest) {
        try {
            servletRequest.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error("Incorrect encoding", e);
        }
    }
}
