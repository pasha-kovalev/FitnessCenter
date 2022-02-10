package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.service.ItemService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowProgramsCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowProgramsCommand.class);
    private final ResponseCreator responseCreator;
    private final ItemService itemService;


    ShowProgramsCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Item> products;
        try {
            products = itemService.findAll();
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        products.forEach(p -> p.setDescription(p.getDescription().replace("\n", "\\")));
        request.addAttributeToJsp(Attribute.PRODUCT_LIST, products);
        CommandHelper.addDiscountListToJsp(request, products, itemService);
        return responseCreator.createForwardResponse(PagePath.SHOW_PROGRAMS);
    }
}