package com.epam.jwd.fitness_center.dao;

import com.epam.jwd.fitness_center.exception.DaoException;

import java.sql.PreparedStatement;

@FunctionalInterface
public interface  StatementPreparator {
    void accept(PreparedStatement t) throws DaoException;
}
