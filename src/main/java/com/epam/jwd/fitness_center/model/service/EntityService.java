package com.epam.jwd.fitness_center.model.service;

import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.entity.Entity;

import java.util.List;
import java.util.Optional;

public interface EntityService<T extends Entity> {

    List<T> findAll() throws ServiceException;

    boolean update(T entity) throws ServiceException;

    T insert(T entity) throws ServiceException;

}
