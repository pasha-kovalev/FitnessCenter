package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Program;
import com.epam.jwd.fitness_center.model.entity.ProgramStatus;
import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.util.Optional;

public interface ProgramService extends EntityService<Program> {
    Program insert(Long orderId, ProgramStatus status, String intensity, String schedule, String exercises,
                   String diet, String equipment) throws ServiceException;

    boolean update(Program program, String intensity, String schedule,
                   String exercises, String diet, String equipment, UserRole role) throws ServiceException;

    Optional<Program> findByOrderAndClientId(Long orderId, Long clientId) throws ServiceException;

    Optional<Program> find(long id) throws ServiceException;

}
