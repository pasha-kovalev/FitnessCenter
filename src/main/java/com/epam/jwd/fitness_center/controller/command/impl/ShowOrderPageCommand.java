package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
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

    ShowOrderPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Item> products;
        try {
            products = itemService.findAll();
        } catch (ServiceException e) {
            LOG.error("Error during registering new user", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addAttributeToJsp(RequestParameter.PRODUCT_LIST, products);
        System.out.println(products);
        addDiscountListToJsp(request, products);
        System.out.println(products);
        return requestFactory.createForwardResponse(PagePath.SHOW_ORDER);
    }

    private void addDiscountListToJsp(CommandRequest request, List<Item> products)  {
        ArrayList<Item> clonedProductList = new ArrayList<>();
        try {
            for(Item item : products) {
                clonedProductList.add(item.clone());
            }
        } catch (CloneNotSupportedException e) {
            LOG.error("Unable to clone products list", e);
        }

        Optional<Object> optionalUserDetails = request.retrieveFromSession(SessionAttribute.USER_DETAILS);
        if(optionalUserDetails.isPresent()) {
            BigDecimal discount = ((UserDetails) optionalUserDetails.get()).getDiscount();
            if(discount.compareTo(BigDecimal.ZERO) > 0) {
                request.addAttributeToJsp(RequestParameter.PRODUCT_LIST_WITH_DISCOUNT,
                                          itemService.modifyItemsByDiscount(clonedProductList, discount));
            }
        }
    }
}