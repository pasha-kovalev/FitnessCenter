package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import com.epam.jwd.fitness_center.model.service.UserService;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowOrderPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowOrderPageCommand.class);
    private final RequestFactory requestFactory;
    private final ItemServiceImpl itemService;
    private final UserService userService;

    ShowOrderPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
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
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addAttributeToJsp(Attribute.PRODUCT_LIST, products);
        request.addAttributeToJsp(Attribute.TRAINER_LIST, trainers);
        addDiscountListToJsp(request, products);
        return requestFactory.createForwardResponse(PagePath.SHOW_ORDER);
    }

    private void addDiscountListToJsp(CommandRequest request, List<Item> products) {
        ArrayList<Item> clonedProductList = new ArrayList<>();
        try {
            for (Item item : products) {
                clonedProductList.add(item.clone());
            }
        } catch (CloneNotSupportedException e) {
            LOG.error("Unable to clone products list", e);
        }
        Optional<Object> optionalUserDetails = request.retrieveFromSession(Attribute.USER_DETAILS);
        if (optionalUserDetails.isPresent()) {
            BigDecimal discount = ((UserDetails) optionalUserDetails.get()).getDiscount();
            if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
                request.addAttributeToJsp(Attribute.PRODUCT_LIST_WITH_DISCOUNT,
                        itemService.modifyItemsByDiscount(clonedProductList, discount));
            }
        }
    }
}