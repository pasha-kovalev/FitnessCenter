package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.entity.Token;
import com.epam.jwd.fitness_center.model.entity.UserDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserDetailsDaoImpl extends BaseDao<UserDetails> {
    private static final Logger LOG = LogManager.getLogger(UserDetailsDaoImpl.class);
    private static final String TABLE_NAME = "user_details";
    private static final String USER_ID_FIELD_NAME = "user_id";
    private static final String CARD_ID_FIELD_NAME = "card_id";
    private static final String DISCOUNT_FIELD_NAME = "discount";
    private static final String TRAINER_ID_FIELD_NAME = "personal_trainer_id";

    private static final List<String> TABLE_FIELDS = Arrays.asList(
            USER_ID_FIELD_NAME, CARD_ID_FIELD_NAME,  DISCOUNT_FIELD_NAME, TRAINER_ID_FIELD_NAME
    );

    private static final String DELETE_BY_USER_ID_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE "
            + USER_ID_FIELD_NAME +" = ?";

    private final String SELECT_BY_USER_ID_QUERY = selectAllQuery + " WHERE "
            + USER_ID_FIELD_NAME +" = ?";

//todo getFields
    private static final String INSERT_NEW_USER_DETAILS_QUERY = "INSERT INTO " + TABLE_NAME +
            " (user_id, card_id, discount, personal_trainer_id)\n" +
            "    VALUE (?, ?, ?, ?)";

    private static final String UPDATE_QUERY_ADDITION = "user_id = ?, card_id = ?, discount = ?, personal_trainer_id = ?";

    UserDetailsDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
        updateQuery = String.format(updateQuery, UPDATE_QUERY_ADDITION);
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
        statement.setLong(2, details.getCardId());
        statement.setBigDecimal(3, details.getDiscount());
        statement.setLong(4, details.getPersonalTrainerId());
    }

    @Override
    protected UserDetails extractResult(ResultSet rs) throws DaoException {
        try {
            return new UserDetails(
                    rs.getLong(USER_ID_FIELD_NAME),
                    rs.getLong(CARD_ID_FIELD_NAME),
                    rs.getBigDecimal(DISCOUNT_FIELD_NAME),
                    rs.getLong(TRAINER_ID_FIELD_NAME)
            );
        } catch (SQLException e) {
            throw new DaoException("Unable to extract user details", e);
        }
    }

    @Override
    public boolean update(UserDetails details) throws DaoException {
        int rows =  executeUpdate(updateQuery, st -> {
            fillEntity(st, details);
        });
        return rows > 0;
    }

    public List<UserDetails> findByUserId(Long userId) throws DaoException {
        return executePrepared(SELECT_BY_USER_ID_QUERY, this::extractResult, st -> st.setLong(1, userId) );
    }
}
