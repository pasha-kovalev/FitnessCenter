package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.entity.Token;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TokenDaoImpl extends BaseDao<Token> {
    private static final Logger LOG = LogManager.getLogger(TokenDaoImpl.class);
    private static final String TOKEN_TABLE_NAME = "user_token";
    private static final String ID_FIELD_NAME = "id";
    private static final String USER_ID_FIELD_NAME = "user_id";
    private static final String VALUE_FIELD_NAME = "token";
    private static final String CREATION_DATE_FIELD_NAME = "creation_date";

    private static final List<String> TOKEN_FIELDS = Arrays.asList(
            ID_FIELD_NAME, USER_ID_FIELD_NAME, CREATION_DATE_FIELD_NAME, VALUE_FIELD_NAME
    );

    private static final String DELETE_BY_USER_ID_QUERY = "DELETE FROM " + TOKEN_TABLE_NAME + " WHERE "
            + USER_ID_FIELD_NAME + " = ?";
    private static final String DELETE_BY_DAYS_QUERY = "DELETE FROM " + TOKEN_TABLE_NAME + " WHERE "
            + CREATION_DATE_FIELD_NAME + " < now() - interval ? DAY";
    private static final String INSERT_NEW_USER_TOKEN_QUERY = "INSERT INTO " + TOKEN_TABLE_NAME +
            " (id, user_id, token, creation_date)\n" +
            "    VALUE (NULL, ?,?, DEFAULT)";
    private static final String UPDATE_QUERY_ADDITION = "user_token = ?, creation_date = ?";
    private final String SELECT_BY_USER_ID_QUERY = selectAllQuery + " WHERE "
            + USER_ID_FIELD_NAME + " = ?";

    {
        updateQuery = String.format(updateQuery, UPDATE_QUERY_ADDITION);
        insertQuery = INSERT_NEW_USER_TOKEN_QUERY;
    }

    TokenDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return TOKEN_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return TOKEN_FIELDS;
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Token token) throws SQLException {
        statement.setLong(1, token.getUserId());
        statement.setString(2, token.getValue());
    }

    @Override
    protected Token extractResult(ResultSet rs) throws DaoException {
        try {
            return new Token(
                    rs.getLong(ID_FIELD_NAME),
                    rs.getLong(USER_ID_FIELD_NAME),
                    rs.getString(VALUE_FIELD_NAME),
                    rs.getTimestamp(CREATION_DATE_FIELD_NAME).toLocalDateTime()
            );
        } catch (SQLException e) {
            throw new DaoException("Unable to extract user token", e);
        }
    }

    @Override
    public boolean update(Token token) throws DaoException {
        int rows = executeUpdate(updateQuery, st -> {
            fillEntity(st, token);
            st.setLong(3, token.getId());
        });
        return rows > 0;
    }

    //todo interface
    public long removeByUserId(Long userId) throws DaoException {
        return executeUpdate(DELETE_BY_USER_ID_QUERY, st -> st.setLong(1, userId));
    }

    public List<Token> findByUserId(Long userId) throws DaoException {
        return executePrepared(SELECT_BY_USER_ID_QUERY, this::extractResult, st -> st.setLong(1, userId));
    }

    public void removeExpiredToken(int days) throws DaoException {
        executeUpdate(DELETE_BY_DAYS_QUERY, st -> st.setLong(1, days));
    }
}
