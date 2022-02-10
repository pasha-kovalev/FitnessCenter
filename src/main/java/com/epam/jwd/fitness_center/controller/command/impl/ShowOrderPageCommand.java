package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.service.ItemService;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowOrderPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowOrderPageCommand.class);
    private final ResponseCreator responseCreator;
    private final ItemService itemService;
    private final UserService userService;

    ShowOrderPageCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        itemService = ServiceProvider.getInstance().getItemService();
        userService = ServiceProvider.getInstance().getUserService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Item> products;
        List<User> trainers;
        try {
            products = itemService.findAll();
            trainers = userService.findActiveTrainers();
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        request.addAttributeToJsp(Attribute.PRODUCT_LIST, products);
        request.addAttributeToJsp(Attribute.TRAINER_LIST, trainers);
        CommandHelper.addDiscountListToJsp(request, products, itemService);
        return responseCreator.createForwardResponse(PagePath.SHOW_ORDER);
    }

}