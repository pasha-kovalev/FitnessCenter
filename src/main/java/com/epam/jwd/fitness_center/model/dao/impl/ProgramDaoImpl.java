package com.epam.jwd.fitness_center.model.dao.impl;
import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.entity.*;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProgramDaoImpl extends BaseDao<Program> {
    private static final Logger LOG = LogManager.getLogger(ProgramDaoImpl.class);
    private static final String TABLE_NAME = "program";
    private static final String ORDER_ID_FIELD_NAME = "order_id";
    private static final String STATUS_FIELD_NAME = "status";
    private static final String INTENSITY_FIELD_NAME = "intensity";
    private static final String SCHEDULE_FIELD_NAME = "schedule";
    private static final String EXERCISES_FIELD_NAME = "exercises";
    private static final String DIET_FIELD_NAME = "diet";
    private static final String EQUIPMENT_FIELD_NAME = "equipment";
    private static final String STARTS_AT_FIELD_NAME = "starts_at";
    private static final String UPDATED_BY_USER_ROLE_ID_FIELD_NAME = "account_role";
    private static final String ENDS_AT_FIELD_NAME = "ends_at";
    private static final String UPDATED_AT_FIELD_NAME = "updated_at";

    private static final List<String> TABLE_FIELDS = Arrays.asList(
            ORDER_ID_FIELD_NAME,
            STATUS_FIELD_NAME,
            INTENSITY_FIELD_NAME,
            SCHEDULE_FIELD_NAME,
            EXERCISES_FIELD_NAME,
            DIET_FIELD_NAME,
            EQUIPMENT_FIELD_NAME,
            STARTS_AT_FIELD_NAME,
            UPDATED_BY_USER_ROLE_ID_FIELD_NAME,
            ENDS_AT_FIELD_NAME,
            UPDATED_AT_FIELD_NAME
    );

    private static final String SELECT_ALL_PROGRAMS = "SELECT order_id, status, intensity, schedule, exercises, diet, \n" +
            "equipment, starts_at, account_role, ends_at, updated_at\n" +
            "FROM `program`\n" +
            "JOIN program_status s on s.id = program.status_id\n" +
            "JOIN user_role r on r.id = program.updated_by_user_role_id";

    private static final String SELECT_PROGRAM_BY_ID = "SELECT order_id, status, intensity, schedule, exercises, diet, \n" +
            "equipment, starts_at, account_role, ends_at, updated_at\n" +
            "FROM `program`\n" +
            "JOIN program_status s on s.id = program.status_id\n" +
            "JOIN user_role r on r.id = program.updated_by_user_role_id\n" +
            "WHERE `program`.order_id = ?";

    private static final String INSERT_NEW_PROGRAM_QUERY = "insert into program (order_id, status_id, intensity, " +
            "schedule, exercises, diet, equipment, starts_at, updated_by_user_role_id, ends_at, updated_at)\n" +
            "value (?, " +
            "(SELECT id FROM program_status WHERE status=?)," +
            " ?, ?, ?, ?, ?, ?, " +
            "(SELECT id FROM user_role WHERE account_role=?), " +
            "?, default)";

    private static final String UPDATE_PROGRAM_BY_ID = "update program\n" +
            "set status_id = (SELECT id FROM program_status WHERE status=?), " +
            "intensity = ?, schedule = ?, exercises = ?, diet = ?, equipment = ?, starts_at = ?,\n" +
            "    updated_by_user_role_id = (SELECT id FROM user_role WHERE account_role=?), " +
            "ends_at = ?, updated_at = CURRENT_TIMESTAMP\n" +
            "where order_id = ?";

    {
        selectAllQuery = SELECT_ALL_PROGRAMS;
        insertQuery = INSERT_NEW_PROGRAM_QUERY;
        selectByIdQuery = SELECT_PROGRAM_BY_ID;
        updateQuery = UPDATE_PROGRAM_BY_ID;
    }

    ProgramDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return TABLE_FIELDS;
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Program entity) throws SQLException {
        statement.setLong(1, entity.getOrder().getId());
        statement.setString(2, entity.getProgramStatus().name().toLowerCase());
        statement.setString(3, entity.getIntensity());
        statement.setString(4, entity.getSchedule());
        statement.setString(5, entity.getExercises());
        statement.setString(6, entity.getDiet());
        statement.setString(7, entity.getEquipment());
        statement.setTimestamp(8, java.sql.Timestamp.valueOf(entity.getStartsAt()));
        statement.setString(9, entity.getRoleUpdatedBy().name().toLowerCase());
        statement.setTimestamp(10, java.sql.Timestamp.valueOf(entity.getEndsAt()));
    }

    @Override
    protected Program extractResult(ResultSet rs) throws DaoException {
        try {
            return new Program.Builder()
                    .setOrder(ServiceProvider.getInstance()
                            .getOrderService()
                            .findOrderById(rs.getLong(ORDER_ID_FIELD_NAME))
                            .orElseThrow(() -> new DaoException("Unable to set order in program")))
                    .setProgramStatus(ProgramStatus.valueOf(rs.getString(STATUS_FIELD_NAME).toUpperCase()))
                    .setIntensity(rs.getString(INTENSITY_FIELD_NAME))
                    .setSchedule(rs.getString(SCHEDULE_FIELD_NAME))
                    .setExercises(rs.getString(EXERCISES_FIELD_NAME))
                    .setDiet(rs.getString(DIET_FIELD_NAME))
                    .setEquipment(rs.getString(EQUIPMENT_FIELD_NAME))
                    .setStartsAt(rs.getTimestamp(STARTS_AT_FIELD_NAME).toLocalDateTime())
                    .setRoleUpdatedBy(UserRole.valueOf(rs.getString(UPDATED_BY_USER_ROLE_ID_FIELD_NAME).toUpperCase()))
                    .setEndsAt(rs.getTimestamp(ENDS_AT_FIELD_NAME).toLocalDateTime())
                    .setUpdatedAt(rs.getTimestamp(UPDATED_AT_FIELD_NAME).toLocalDateTime())
                    .build();
        } catch (SQLException | ServiceException e) {
            throw new DaoException("Unable to extract program", e);
        }
    }

    @Override
    public boolean update(Program entity) throws DaoException {
        int rows = executeUpdate(updateQuery, st -> {
            fillEntity(st, entity);
            st.setLong(11, entity.getOrder().getId());
        });
        return rows > 0;
    }

    @Override
    public Program create(Program entity) throws DaoException {
        executeUpdate(insertQuery, st -> fillEntity(st, entity));
        Optional<Program> optionalEntity = read(entity.getOrder().getId());
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        } else {
            throw new DaoException("Unable to create entity");
        }
    }
}
