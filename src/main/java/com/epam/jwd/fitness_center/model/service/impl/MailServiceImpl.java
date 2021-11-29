package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.controller.command.SessionAttribute;
import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.dao.impl.TokenDaoImpl;
import com.epam.jwd.fitness_center.model.dao.impl.UserDaoImpl;
import com.epam.jwd.fitness_center.model.entity.Token;
import com.epam.jwd.fitness_center.model.service.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

public class MailServiceImpl implements MailService {
    private static final Logger LOG = LogManager.getLogger(MailServiceImpl.class);
    private static final String MAIL_CONFIG_PATH = "properties/mail/config.properties";
    public static final String MAIL_SESSION_CONFIG_PATH = "properties/mail/session.properties";
    public static final String CONTENT_TYPE = "text/plain; charset=UTF-8";
    public static final String MAIL_USER_NAME_PROPERTY = "mail.user.name";
    public static final String MAIL_USER_PASSWORD_PROPERTY = "mail.user.password";
    public static final String MAIL_LINK_TEMPLATE_PROPERTY = "mail.link.template";

    //todo bundle, processing exceptions during reading/writing
    @Override
    public long sendConfirmationEmail(long userId, String email, SessionAttribute locale) throws ServiceException {
        Properties mailProperties = takeMailProperties(MAIL_CONFIG_PATH);
        String login = mailProperties.getProperty(MAIL_USER_NAME_PROPERTY);
        String password = mailProperties.getProperty(MAIL_USER_PASSWORD_PROPERTY);
        String linkTemplate = mailProperties.getProperty(MAIL_LINK_TEMPLATE_PROPERTY);
        final String token = UUID.randomUUID().toString();
        long tokenId = sendToDatabase(userId, token);
        MimeMessage message = new MimeMessage(createMailSession(login, password));
        String messageText = "Welcome to Training Center! Follow the link below (valid for 24 hours):\n" +
                String.format(linkTemplate, tokenId, token);
        sendMessage(userId, email, message, messageText);
        return tokenId;
    }

    private void sendMessage(long id, String email, MimeMessage message, String messageText) throws ServiceException {
        try {
            message.setSubject("Email confirmation");
            message.setContent(messageText, CONTENT_TYPE);
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            Transport.send(message);
        } catch (MessagingException e) {
            LOG.error("Unable to send message. User id:{}", id, e);
            throw new ServiceException("Unable to send message", e);
        }
    }

    private long sendToDatabase(long userId, String token) throws ServiceException {
        final TokenDaoImpl tokenDao = DaoProvider.getInstance().getTokenDao();
        try {
            return tokenDao.create(new Token(userId, token)).getId();
        } catch (DaoException e) {
            LOG.error("Unable to insert user token. User ID: {}", userId, e);
            throw new ServiceException("Unable to insert user token", e);
        }
    }

    private Properties takeMailProperties(String path) throws ServiceException {
        try(InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if(is == null) {
                LOG.warn("Unable to find mail service property file:{}", path);
                throw new ServiceException("Unable to find mail service property file:" + path);
            }
            Properties mailProperties =  new Properties();
            mailProperties.load(is);
            return mailProperties;
        } catch (IOException e) {
            LOG.warn("Unable to open connection mail service property file:{}", path);
            throw new ServiceException("Unable to open connection mail service property file", e);
        }
    }

    private Session createMailSession(String login, String password) throws ServiceException {

        return Session.getDefaultInstance(takeSessionProperties(MAIL_SESSION_CONFIG_PATH),
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(login, password);
                    }
                });
    }

    private Properties takeSessionProperties(String path) throws ServiceException {

        try(InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if(is == null) {
                LOG.warn("Unable to find mail session service property file:{}", path);
                throw new ServiceException("Unable to find mail session service property file:" + path);
            }
            Properties sessionProperties =  new Properties();
            sessionProperties.load(is);
            return sessionProperties;
        } catch (IOException e) {
            LOG.warn("Unable to open connection mail session service property file:{}", path);
            throw new ServiceException("Unable to open connection mail session service property file", e);
        }
    }

}
