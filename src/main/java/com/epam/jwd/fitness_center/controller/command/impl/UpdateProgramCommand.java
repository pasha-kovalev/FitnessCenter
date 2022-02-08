package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.ResponseCreator;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UpdateProgramCommand implements Command {
    public static final String PROGRAM_NOT_CHANGED_MARKER = "false";
    public static final String PROGRAM_REFUSED_MARKER = "refused";
    private static final Logger LOG = LogManager.getLogger(UpdateProgramCommand.class);
    private final ResponseCreator responseCreator;
    private final OrderService orderService;
    private final ProgramService programService;

    UpdateProgramCommand(ResponseCreator responseCreator) {
        this.responseCreator = responseCreator;
        orderService = ServiceProvider.getInstance().getOrderService();
        programService = ServiceProvider.getInstance().getProgramService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String programChangeMarker = request.getParameter(RequestParameter.PROGRAM_CHANGE_MARKER);
        String comment = request.getParameter(RequestParameter.COMMENT);
        Optional<Long> orderIdOptional = CommandHelper.retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent() || programChangeMarker == null) {
            return responseCreator.createRedirectResponse(PagePath.ERROR);
        }
        Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
        long orderId = orderIdOptional.get();
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!optionalUser.isPresent() || !optionalOrder.isPresent()) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            Order order = optionalOrder.get();
            User user = (User) optionalUser.get();
            Optional<Program> programOptional;
            if (order.getOrderStatus() != OrderStatus.PENDING_CLIENT
                    && order.getOrderStatus() != OrderStatus.PENDING_TRAINER) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            if (user.getRole() == UserRole.TRAINER) {
                if (!order.getAssignmentTrainerId().equals(user.getId()) && !order.getTrainerId().equals(user.getId())) {
                    return CommandHelper.createInfoErrorResponse(responseCreator, request);
                }
                programOptional = programService.find(orderId);
            } else {
                programOptional = programService.findByOrderAndClientId(orderId, user.getId());
            }
            if (!programOptional.isPresent()) {
                return CommandHelper.createInfoErrorResponse(responseCreator, request);
            }
            Optional<CommandResponse> optionalCommandResponse = processProgram(request, programChangeMarker, comment,
                    orderId, order, user, programOptional.get());
            if (optionalCommandResponse.isPresent()) {
                return optionalCommandResponse.get();
            }
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return responseCreator.createRedirectResponse(PagePath.ERROR500);
        }
        return responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
    }

    private Optional<CommandResponse> processProgram(CommandRequest request, String programChangeMarker, String comment,
                                                     long orderId, Order order, User user,
                                                     Program program) throws ServiceException {
        request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_SUCCESS);
        if (programChangeMarker.equals(PROGRAM_NOT_CHANGED_MARKER)) {
            orderService.updateOrderStatus(OrderStatus.ACTIVE, orderId);
            return Optional.of(responseCreator.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT));
        }
        if (programChangeMarker.equals(PROGRAM_REFUSED_MARKER)) {
            program.setProgramStatus(ProgramStatus.REFUSED);
            programService.update(program);
            order.setComment(comment);
        } else {
            if (!updateProgram(request, program, user.getRole())) {
                return Optional.of(responseCreator.createRedirectResponse(PagePath.ERROR));
            }
        }
        if (user.getRole() == UserRole.TRAINER) {
            order.setOrderStatus(OrderStatus.PENDING_CLIENT);
        } else {
            order.setOrderStatus(OrderStatus.PENDING_TRAINER);
        }
        orderService.update(order);
        return Optional.empty();
    }

    private boolean updateProgram(CommandRequest request, Program program, UserRole role) {
        final String schedule = request.getParameter(RequestParameter.SCHEDULE);
        final String intensity = request.getParameter(RequestParameter.INTENSITY);
        final String exercises = request.getParameter(RequestParameter.EXERCISES);
        final String diet = request.getParameter(RequestParameter.DIET);
        final String equipment = request.getParameter(RequestParameter.EQUIPMENT);
        boolean result;
        try {
            result = programService.update(program, intensity, schedule, exercises, diet, equipment, role);
        } catch (ServiceException e) {
            LOG.error(e);
            return false;
        }
        return result;
    }
}
