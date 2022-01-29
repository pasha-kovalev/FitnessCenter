package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ManageItemDataCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ManageItemDataCommand.class);

    private final RequestFactory requestFactory;
    private final ItemServiceImpl itemService;

    ManageItemDataCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String itemName = request.getParameter(RequestParameter.NAME);
        String itemPrice = request.getParameter(RequestParameter.PRICE);
        Optional<Long> itemIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ID);
        if (!itemIdOptional.isPresent() || itemName == null || itemPrice == null) {
            return requestFactory.createRedirectResponse(PagePath.ERROR);
        }
        long itemId = itemIdOptional.get();
        try {
            if (!itemService.update(itemId, itemName, itemPrice)) {
                return requestFactory.createRedirectResponse(PagePath.ERROR);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createRedirectResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return requestFactory.createAjaxResponse(json);
    }
}