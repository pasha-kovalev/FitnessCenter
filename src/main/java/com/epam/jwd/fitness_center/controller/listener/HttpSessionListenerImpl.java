package com.epam.jwd.fitness_center.controller.listener;

import com.epam.jwd.fitness_center.controller.command.RequestParameter;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.epam.jwd.fitness_center.controller.command.Attribute.GLOBAL_SESSION_MAP;

@WebListener
public class HttpSessionListenerImpl implements HttpSessionListener {

    /**Retrieves session map from servlet context (or creates if null)
     * where key is user id and value is session of user
     * @param appContext servlet context
     * @return session map
     */
    public static Map<Long, HttpSession> getSessionMap(ServletContext appContext) {
        Map<Long, HttpSession> sessionMap = (Map<Long, HttpSession>) appContext.getAttribute(GLOBAL_SESSION_MAP);
        if (sessionMap == null) {
            sessionMap = new ConcurrentHashMap<>();
            appContext.setAttribute(GLOBAL_SESSION_MAP, sessionMap);
        }
        return sessionMap;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        setDefaultLocale(session);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        removeFromSessionMap(se);
    }

    /**Removes session from session map
     * @param se http session event
     */
    private void removeFromSessionMap(HttpSessionEvent se) {
        Map<Long, HttpSession> sessionMap = getSessionMap(se.getSession().getServletContext());
        sessionMap.values().remove(se.getSession());
    }

    /**Sets default locale to session
     * @param session http session event
     */
    private void setDefaultLocale(HttpSession session) {
        session.setAttribute(RequestParameter.LOCALE, Locale.getDefault().toString());
    }
}
