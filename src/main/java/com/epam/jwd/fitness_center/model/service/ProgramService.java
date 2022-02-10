package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Program;
import com.epam.jwd.fitness_center.model.entity.ProgramStatus;
import com.epam.jwd.fitness_center.model.entity.UserRole;

import java.util.Optional;

/**
 * The interface represents program service
 */
public interface ProgramService extends EntityService<Program> {
    /**
     * Inserts new program
     *
     * @param orderId   order id
     * @param status    program status
     * @param intensity program intensity
     * @param schedule  program schedule
     * @param exercises program exercises
     * @param diet      program diet
     * @param equipment program equipment
     * @return inserted program
     * @throws ServiceException while unable to perform a service action
     */
    Program insert(Long orderId, ProgramStatus status, String intensity, String schedule, String exercises,
                   String diet, String equipment) throws ServiceException;

    /**
     * Updates program
     *
     * @param program   program
     * @param intensity program intensity
     * @param schedule  program schedule
     * @param exercises program exercises
     * @param diet      program diet
     * @param equipment program equipment
     * @param role      user role (who updated)
     * @return true if updated
     * @throws ServiceException while unable to perform a service action
     */
    boolean update(Program program, String intensity, String schedule,
                   String exercises, String diet, String equipment, UserRole role) throws ServiceException;

    /**
     * Finds program with given order id and client id
     *
     * @param orderId  order id
     * @param clientId client id
     * @return optional with program or optional empty
     * @throws ServiceException while unable to perform a service action
     */
    Optional<Program> findByOrderAndClientId(Long orderId, Long clientId) throws ServiceException;

    /**
     * Finds program by id
     *
     * @param id program id
     * @return optional with program or optional empty
     * @throws ServiceException while unable to perform a service action
     */
    Optional<Program> find(long id) throws ServiceException;
}
