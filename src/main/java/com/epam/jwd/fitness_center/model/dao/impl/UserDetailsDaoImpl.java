package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UserDetailsDaoImpl extends BaseDao<UserDetails> {
    private static final Logger LOG = LogManager.getLogger(UserDetailsDaoImpl.class);
    private static final String TABLE_NAME = "user_details";
    private static final String USER_ID_FIELD_NAME = "user_id";
    private static final String DISCOUNT_FIELD_NAME = "discount";
    private static final String TRAINER_ID_FIELD_NAME = "personal_trainer_id";

    private static final List<String> TABLE_FIELDS = Arrays.asList(
            USER_ID_FIELD_NAME, DISCOUNT_FIELD_NAME, TRAINER_ID_FIELD_NAME
    );

    private static final String INSERT_NEW_USER_DETAILS_QUERY = "INSERT INTO " + TABLE_NAME +
            " (user_id, discount, personal_trainer_id)\n" +
            "    VALUE (?, default, ?)";
    private static final String UPDATE_QUERY = "update " + TABLE_NAME + " set discount = ?, " +
            "personal_trainer_id = ? where user_id = ?";
    private final String SELECT_BY_USER_ID_QUERY = selectAllQuery + " WHERE "
            + USER_ID_FIELD_NAME + " = ?";

    UserDetailsDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
        selectByIdQuery = SELECT_BY_USER_ID_QUERY;
        updateQuery = UPDATE_QUERY;
        insertQuery = INSERT_NEW_USER_DETAILS_QUERY;
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
    protected void fillEntity(PreparedStatement statement, UserDetails details) throws SQLException {
        statement.setLong(1, details.getUserId());
        statement.setBigDecimal(2, details.getDiscount());
        if (details.getPersonalTrainerId() == null) {
            statement.setNull(3, Types.INTEGER);
        } else {
            statement.setLong(3, details.getPersonalTrainerId());
        }
    }

    @Override
    protected UserDetails extractResult(ResultSet rs) throws DaoException {
        try {
            return new UserDetails(
                    rs.getLong(USER_ID_FIELD_NAME),
                    rs.getBigDecimal(DISCOUNT_FIELD_NAME),
                    rs.getLong(TRAINER_ID_FIELD_NAME)
            );
        } catch (SQLException e) {
            throw new DaoException("Unable to extract user details", e);
        }
    }

    @Override
    public boolean update(UserDetails details) throws DaoException {
        int rows = executeUpdate(updateQuery, st -> {
            st.setBigDecimal(1, details.getDiscount());
            if (details.getPersonalTrainerId() == null) {
                st.setNull(2, Types.INTEGER);
            } else {
                st.setLong(2, details.getPersonalTrainerId());
            }
            st.setLong(3, details.getUserId());
        });
        return rows > 0;
    }

    @Override
    public UserDetails create(UserDetails entity) throws DaoException {
        executeUpdate(insertQuery, st -> fillEntity(st, entity));
        Optional<UserDetails> optionalEntity = read(entity.getUserId());
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        } else {
            throw new DaoException("Unable to create entity");
        }
    }

    public List<UserDetails> findByUserId(Long userId) throws DaoException {
        return executePrepared(SELECT_BY_USER_ID_QUERY, this::extractResult, st -> st.setLong(1, userId));
    }
}
