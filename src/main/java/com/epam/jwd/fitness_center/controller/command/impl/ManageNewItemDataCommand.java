package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.RequestParameter;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ManageNewItemDataCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ManageNewItemDataCommand.class);

    private final RequestFactory requestFactory;
    private final ItemServiceImpl itemService;

    ManageNewItemDataCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String itemName = request.getParameter(RequestParameter.NAME);
        String itemPrice = request.getParameter(RequestParameter.PRICE);
        if (itemName == null || itemPrice == null) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        try {
            itemService.insert(itemName, itemPrice);
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(itemName);
        return requestFactory.createAjaxResponse(json);
    }
}