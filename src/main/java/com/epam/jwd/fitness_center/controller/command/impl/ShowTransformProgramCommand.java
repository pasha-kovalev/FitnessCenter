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
import java.util.stream.Collectors;

public class ShowTransformProgramCommand implements Command {
    public static final String TRANSFORMATION_PROGRAM_NAME = "transformation";
    private static final Logger LOG = LogManager.getLogger(ShowTransformProgramCommand.class);
    private final ResponseCreator responseCreator;
    private final ItemService itemService;

    ShowTransformProgramCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        itemService = ServiceProvider.getInstance().getItemService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        List<Item> items;
        Item transformItem;
        try {
            items = itemService.findAll()
                    .stream()
                    .filter(i -> i.getName().equals(TRANSFORMATION_PROGRAM_NAME))
                    .collect(Collectors.toList());
        } catch (ServiceException e) {
            LOG.error(e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        if (items.isEmpty()) {
            LOG.error("Incorrect program name: " + TRANSFORMATION_PROGRAM_NAME);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        transformItem = items.get(0);
        transformItem.setDescription(transformItem.getDescription().replace("\n", "\\"));
        request.addAttributeToJsp(Attribute.ITEM, transformItem);
        CommandHelper.addDiscountListToJsp(request, items, itemService);
        return responseCreator.createForwardResponse(PagePath.SHOW_TRANSFORM_PROGRAM);
    }
}