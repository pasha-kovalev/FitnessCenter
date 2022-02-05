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
import java.util.stream.Collectors;

public class ShowTransformProgramCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(ShowTransformProgramCommand.class);
    public static final String TRANSFORMATION_PROGRAM_NAME = "transformation";
    private final ResponseCreator responseCreator;
    private final ItemServiceImpl itemService;

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
        if(items.isEmpty()) {
            LOG.error("Incorrect program name: " + TRANSFORMATION_PROGRAM_NAME);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        transformItem = items.get(0);
        transformItem.setDescription(transformItem.getDescription().replace("\n", "\\"));
        request.addAttributeToJsp(Attribute.ITEM, transformItem);
        return responseCreator.createForwardResponse(PagePath.SHOW_TRANSFORM_PROGRAM);
    }
}