package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.service.ItemService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ManageItemDataCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ManageItemDataCommand.class);

    private final ResponseCreator responseCreator;
    private final ItemService itemService;

    ManageItemDataCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String itemName = request.getParameter(RequestParameter.NAME);
        String itemPrice = request.getParameter(RequestParameter.PRICE);
        String itemDescription = request.getParameter(RequestParameter.DESCRIPTION);
        Optional<Long> itemIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ID);
        if (!itemIdOptional.isPresent() || itemName == null || itemPrice == null || itemDescription == null) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        long itemId = itemIdOptional.get();
        try {
            if (!itemService.update(itemId, itemName, itemPrice, itemDescription)) {
                return responseCreator.createRedirectResponse(PagePath.ERROR);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(ResourceBundleKey.INFO_SUCCESS);
        return responseCreator.createAjaxResponse(json);
    }
}