package com.epam.jwd.fitness_center.controller.command.impl;

import com.epam.jwd.fitness_center.controller.PagePath;
import com.epam.jwd.fitness_center.controller.RequestFactory;
import com.epam.jwd.fitness_center.controller.command.*;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;

import java.util.Optional;

public class UpdateProgramCommand implements Command {
    public static final String PROGRAM_NOT_CHANGED_MARKER = "false";
    public static final String PROGRAM_REFUSED_MARKER = "refused";
    private final RequestFactory requestFactory;
    private final OrderService orderService;
    private final ProgramService programService;

    UpdateProgramCommand(RequestFactory requestFactory) {
        this.requestFactory = requestFactory;
        orderService = ServiceProvider.getInstance().getOrderService();
        programService = ServiceProvider.getInstance().getProgramService();
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
        String programChangeMarker = request.getParameter(RequestParameter.PROGRAM_CHANGE_MARKER);
        String comment = request.getParameter(RequestParameter.COMMENT);
        Optional<Long> orderIdOptional = retrievePositiveLongParameter(request, RequestParameter.ORDER_ID);
        if (!orderIdOptional.isPresent() || programChangeMarker == null) {
            return requestFactory.createForwardResponse(PagePath.ERROR);
        }
        Optional<Object> optionalUser = request.retrieveFromSession(Attribute.USER);
        long orderId = orderIdOptional.get();
        try {
            Optional<Order> optionalOrder = orderService.findOrderById(orderId);
            if (!optionalUser.isPresent() || !optionalOrder.isPresent())  {
                return createInfoErrorResponse(requestFactory, request);
            }
            Order order = optionalOrder.get();
            User user = (User) optionalUser.get();
            Optional<Program> programOptional;
            if(order.getOrderStatus() != OrderStatus.PENDING_CLIENT
                    && order.getOrderStatus() != OrderStatus.PENDING_TRAINER) {
                return createInfoErrorResponse(requestFactory, request);
            }
            if(user.getRole() == UserRole.TRAINER) {
                if(!order.getAssignmentTrainerId().equals(user.getId()) && !order.getTrainerId().equals(user.getId())) {
                    return createInfoErrorResponse(requestFactory, request);
                }
                programOptional = programService.find(orderId);
            } else {
                programOptional = programService.findByOrderAndClientId(orderId, user.getId());
            }
            if(!programOptional.isPresent()) {
                return createInfoErrorResponse(requestFactory, request);
            }
            Program program = programOptional.get();
            request.addToSession(Attribute.INFO_BUNDLE_KEY, ResourceBundleKey.INFO_SUCCESS);
            if(programChangeMarker.equals(PROGRAM_NOT_CHANGED_MARKER)) {
                orderService.updateOrderStatus(OrderStatus.ACTIVE, orderId);
                return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
            }
            if (programChangeMarker.equals(PROGRAM_REFUSED_MARKER)) {
                program.setProgramStatus(ProgramStatus.REFUSED);
                programService.update(program);
                order.setComment(comment);
            } else {
                if(!updateProgram(request, program, user.getRole())) {
                    return requestFactory.createForwardResponse(PagePath.ERROR500);
                }
            }
            if(user.getRole() == UserRole.TRAINER) {
                order.setOrderStatus(OrderStatus.PENDING_CLIENT);
            } else {
                order.setOrderStatus(OrderStatus.PENDING_TRAINER);
            }
            orderService.update(order);
        } catch (ServiceException e) {
            LOG.error("Error during order confirmation", e);
            return requestFactory.createForwardResponse(PagePath.ERROR500);
        }
        return requestFactory.createRedirectResponse(PagePath.SHOW_INFO_REDIRECT);
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
