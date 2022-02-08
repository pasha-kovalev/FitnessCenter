package com.epam.jwd.fitness_center.model.service.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.dao.impl.DaoProvider;
import com.epam.jwd.fitness_center.model.dao.impl.ProgramDaoImpl;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.OrderService;
import com.epam.jwd.fitness_center.model.service.ProgramService;
import com.epam.jwd.fitness_center.model.util.TextEscapeUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProgramServiceImpl implements ProgramService {
    private final ProgramDaoImpl programDao;

    ProgramServiceImpl() {
        this.programDao = DaoProvider.getInstance().getProgramDao();
    }

    @Override
    public List<Program> findAll() throws ServiceException {
        try {
            return programDao.read();
        } catch (DaoException e) {
            throw new ServiceException("Unable to find all programs", e);
        }
    }

    @Override
    public Optional<Program> find(long id) throws ServiceException {
        try {
            return programDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException("Unable to find program by id", e);
        }
    }

    @Override
    public boolean update(Program entity) throws ServiceException {
        //todo validation on CLIENT and server
        try {
            return programDao.update(entity);
        } catch (DaoException e) {
            throw new ServiceException("Unable to update program", e);
        }
    }

    @Override
    public Program insert(Program entity) throws ServiceException {
        //todo validation
        try {
            return programDao.create(entity);
        } catch (DaoException e) {
            throw new ServiceException("Unable to insert program", e);
        }
    }

    @Override
    public Program insert(Long orderId, ProgramStatus status, String intensity, String schedule, String exercises,
                          String diet, String equipment) throws ServiceException {

        final int DAY_TO_START = 3;
        OrderService orderService = ServiceProvider.getInstance().getOrderService();
        Order order = orderService.findOrderById(orderId)
                .orElseThrow(() -> new ServiceException("Unable to find order by id"));
        orderService.updateOrderStatus(OrderStatus.PENDING_CLIENT, orderId);
        Program program = new Program.Builder()
                .setOrder(order)
                .setProgramStatus(ProgramStatus.PENDING)
                .setEquipment(TextEscapeUtil.escapeHtml(equipment))
                .setDiet(TextEscapeUtil.escapeHtml(diet))
                .setSchedule(TextEscapeUtil.escapeHtml(schedule))
                .setExercises(TextEscapeUtil.escapeHtml(exercises))
                .setIntensity(TextEscapeUtil.escapeHtml(intensity))
                .setStartsAt(LocalDateTime.now().plusDays(DAY_TO_START))
                .setEndsAt(LocalDateTime.now().plusDays(DAY_TO_START).plusMonths(order.getPeriod()))
                .setRoleUpdatedBy(UserRole.TRAINER)
                .build();
        return insert(program);
    }

    @Override
    public boolean update(Program program, String intensity, String schedule,
                          String exercises, String diet, String equipment, UserRole role) throws ServiceException {
        program.setProgramStatus(ProgramStatus.DISPUTED);
        program.setEquipment(TextEscapeUtil.escapeHtml(equipment));
        program.setDiet(TextEscapeUtil.escapeHtml(diet));
        program.setSchedule(TextEscapeUtil.escapeHtml(schedule));
        program.setExercises(TextEscapeUtil.escapeHtml(exercises));
        program.setIntensity(TextEscapeUtil.escapeHtml(intensity));
        program.setRoleUpdatedBy(role);
        return update(program);
    }

    @Override
    public Optional<Program> findByOrderAndClientId(Long orderId, Long clientId) throws ServiceException {
        Order order = ServiceProvider.getInstance().getOrderService().findOrderById(orderId)
                .orElseThrow(() -> new ServiceException("Unable to find order by id: " + orderId));
        if (!order.getUserDetailsId().equals(clientId)) {
            return Optional.empty();
        }
        return findById(orderId);
    }

    public Optional<Program> findById(Long id) throws ServiceException {
        try {
            return programDao.read(id);
        } catch (DaoException e) {
            throw new ServiceException("Error during searching for Program by id", e);
        }
    }

}
