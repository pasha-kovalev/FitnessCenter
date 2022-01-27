package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.Command;
import com.epam.jwd.fitness_center.controller.command.CommandRequest;
import com.epam.jwd.fitness_center.controller.command.CommandResponse;
import com.epam.jwd.fitness_center.controller.command.RequestParameter;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Item;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import com.epam.jwd.fitness_center.model.service.impl.ItemServiceImpl;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import com.epam.jwd.fitness_center.model.util.TextEscapeUtil;
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
        Optional<Long> itemIdOptional = retrievePositiveLongParameter(request, RequestParameter.ID);
        if (!itemIdOptional.isPresent() || itemName == null || itemPrice == null ) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        long itemId = itemIdOptional.get();
        try {
            if(!itemService.update(itemId, itemName, itemPrice)) {
                return requestFactory.createForwardResponse(PagePath.ERROR);
            }
        } catch (ServiceException e) {
            LOG.error(e);
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        String json = new Gson().toJson(itemName);
        return requestFactory.createAjaxResponse(json);
    }
}