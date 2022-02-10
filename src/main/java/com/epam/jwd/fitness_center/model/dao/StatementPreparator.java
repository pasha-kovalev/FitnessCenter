package com.epam.jwd.fitness_center.model.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Functional interface of method that set values to prepared statement
 */
@FunctionalInterface
public interface StatementPreparator {
    /**
     * Accepts prepared statement for preparing
     *
     * @param t prepared statement
     * @throws SQLException when error while set values to prepared statement occurs
     */
    void accept(PreparedStatement t) throws SQLException;
}
