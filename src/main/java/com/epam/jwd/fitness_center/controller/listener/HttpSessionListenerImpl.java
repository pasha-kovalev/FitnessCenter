package com.epam.jwd.fitness_center.controller.listener;

import com.epam.jwd.fitness_center.controller.command.RequestParameter;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Locale;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

    /**Sets default locale to session
     * @param se http session event
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        session.setAttribute(RequestParameter.LOCALE, Locale.getDefault().toString());
    }
}
