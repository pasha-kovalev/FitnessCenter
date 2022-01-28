package com.epam.jwd.fitness_center.app;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPoolManager;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    private static final Logger LOG = LogManager.getLogger(Application.class);

    public static void main(String[] args) throws ServiceException {
        //todo delete me
        UserService us = ServiceProvider.getInstance().getUserService();

        //works
        /*us.findAll().forEach(System.out::println);
        us.confirmUser(5);
        us.findUserById(5L);
        System.err.println(us.findUserById(5L));
        us.updateUserStatus(UserStatus.INACTIVE, 5L);
        System.err.println(us.findUserById(5L));
        System.err.println(us.isEmailRegistered("vitivanov@gmail.com"));
        System.err.println(us.isEmailRegistered("12@gmail.com"));
        System.err.println((us.findUserById((
                us.register("a@a.com", "pass", "new", "new",
                        UserRole.ADMIN, UserStatus.BANNED)
        ).getId())));*/

        ConnectionPoolManager.getInstance().shutDown();
    }

}
