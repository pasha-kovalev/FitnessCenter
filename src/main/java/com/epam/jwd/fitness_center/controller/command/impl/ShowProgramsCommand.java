package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.Attribute;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowProgramsCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowProgramsCommand.class);
    private final ResponseCreator responseCreator;
    private final ItemServiceImpl itemService;


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
        return responseCreator.createForwardResponse(PagePath.SHOW_PROGRAMS);
    }
}