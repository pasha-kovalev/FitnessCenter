package com.epam.jwd.fitness_center.model.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface StatementPreparator {
    void accept(PreparedStatement t) throws SQLException;
}
