package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.TokenDao;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.entity.Token;
import com.epam.jwd.fitness_center.model.service.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.UUID;

public class MailServiceImpl implements MailService {
    private static final Logger LOG = LogManager.getLogger(MailServiceImpl.class);
    private static final String MAIL_CONFIG_PATH = "properties/mail/config.properties";
    private static final String MAIL_SESSION_CONFIG_PATH = "properties/mail/session.properties";
    private static final String CONTENT_TYPE = "text/plain; charset=UTF-8";
    private static final String MAIL_USER_NAME_PROPERTY = "mail.user.name";
    private static final String MAIL_USER_PASSWORD_PROPERTY = "mail.user.password";
    private static final String MAIL_LINK_TEMPLATE_PROPERTY = "mail.link.template";
    private static final String BUNDLE_NAME = "locale";
    private static final String MAIL_MESSAGE_SUBJECT_KEY = "mail.message.subject";
    private static final String MAIL_MESSAGE_TEXT_KEY = "mail.message.text";

    private final Properties mailProperties;
    private final Properties sessionProperties;
    private final String login;
    private final String password;
    private final String linkTemplate;
    private final Session mailSession;

    {
        try {
            sessionProperties = takeSessionProperties(MAIL_SESSION_CONFIG_PATH);
            mailProperties = takeMailProperties(MAIL_CONFIG_PATH);
        } catch (ServiceException e) {
            LOG.fatal("Unable to process mail property files", e);
            throw new RuntimeException("Unable to process mail property files");
        }
        login = mailProperties.getProperty(MAIL_USER_NAME_PROPERTY);
        password = mailProperties.getProperty(MAIL_USER_PASSWORD_PROPERTY);
        linkTemplate = mailProperties.getProperty(MAIL_LINK_TEMPLATE_PROPERTY);
        mailSession = createMailSession(login, password);
    }

    MailServiceImpl() {
    }

    @Override
    public long sendConfirmationEmail(long userId, String email, String localeStr) throws ServiceException {
        final String token = UUID.randomUUID().toString();
        long tokenId = sendToDatabase(userId, token);
        Locale locale = new Locale(localeStr);
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        String messageText = bundle.getString(MAIL_MESSAGE_TEXT_KEY) + "\n" +
                String.format(linkTemplate, tokenId, token);
        sendMessage(email, messageText, bundle.getString(MAIL_MESSAGE_SUBJECT_KEY));
        return tokenId;
    }

    private void sendMessage(String email, String messageText, String subject) throws ServiceException {
        MimeMessage message = new MimeMessage(mailSession);
        try {
            message.setSubject(subject);
            message.setContent(messageText, CONTENT_TYPE);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Transport.send(message);
        } catch (MessagingException e) {
            throw new ServiceException("Unable to send message", e);
        }
    }

    private long sendToDatabase(long userId, String token) throws ServiceException {
        final TokenDao tokenDao = DaoProvider.getInstance().getTokenDao();
        try {
            return tokenDao.create(new Token(userId, token)).getId();
        } catch (DaoException e) {
            throw new ServiceException("Unable to insert user token", e);
        }
    }

    private Properties takeMailProperties(String path) throws ServiceException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new ServiceException("Unable to find mail service property file:" + path);
            }
            Properties mailProperties = new Properties();
            mailProperties.load(is);
            return mailProperties;
        } catch (IOException e) {
            throw new ServiceException("Unable to open connection mail service property file", e);
        }
    }

    private Session createMailSession(String login, String password) {
        return Session.getDefaultInstance(sessionProperties,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
                });
    }

    private Properties takeSessionProperties(String path) throws ServiceException {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new ServiceException("Unable to find mail session service property file:" + path);
            }
            Properties sessionProperties = new Properties();
            sessionProperties.load(is);
            return sessionProperties;
        } catch (IOException e) {
            throw new ServiceException("Unable to open connection mail session service property file", e);
        }
    }
}
