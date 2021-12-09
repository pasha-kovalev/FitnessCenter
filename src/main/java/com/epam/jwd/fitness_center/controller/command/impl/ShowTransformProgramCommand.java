package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.RequestParameter;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowTransformProgramCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowTransformProgramCommand.class);

    private final RequestFactory requestFactory;
    private final ItemServiceImpl itemService;


    ShowTransformProgramCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        itemService = ServiceProvider.getInstance().getItemService();

    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Item> products;
        try {
            products = itemService.findAll();
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        request.addAttributeToJsp(RequestParameter.PRODUCT_LIST, products);
        return requestFactory.createForwardResponse(PagePath.SHOW_TRANSFORM_PROGRAM);
    }
}