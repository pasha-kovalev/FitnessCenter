package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ShowManageItemsPageCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowManageItemsPageCommand.class);
    private final RequestFactory requestFactory;
    private final ItemServiceImpl itemService;

    ShowManageItemsPageCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Item> items;
        try {
            items = itemService.findAll();
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        String json = new Gson().toJson(items);
        return requestFactory.createAjaxResponse(json);
    }
}