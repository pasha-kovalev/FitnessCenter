package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.entity.Entity;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public abstract class BaseDao<T extends Entity> implements EntityDao<T> {

    //todo make %s [string join getFields()] insteadOf *
    protected static final String SELECT_ALL_FROM = ("select %s from ");
    protected static final String UPDATE = "update ";
    protected static final String SET = "set ";
    protected static final String WHERE_ID = "where id = ?";
    protected static final String INSERT_INTO = "insert into %s (%s) ";
    protected static final String SPACE = " ";
    protected static final String COMMA = ", ";
    protected static final String DELETE_FROM = "DELETE FROM ";

    protected static final int FIRST_COLUMN_INDEX = 1;
    protected final ConnectionPool pool;
    private final Logger logger;
    protected String insertQuery;
    protected String updateQuery;
    protected String selectAllQuery;
    protected String selectByIdQuery;
    private final String deleteById;

    protected BaseDao(ConnectionPool pool, Logger logger) {
        this.pool = pool;
        this.logger = logger;

        insertQuery = String.format(INSERT_INTO, getTableName(), String.join(COMMA, getFields()));
        selectAllQuery = String.format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName();
        selectByIdQuery = selectAllQuery + SPACE + WHERE_ID;
        updateQuery = UPDATE + getTableName() + SPACE + SET + "%s" + WHERE_ID;
        deleteById = DELETE_FROM + getTableName() + SPACE + WHERE_ID;
    }

    @Override
    public T create(T entity) throws DaoException {
        final long entityId = executeInsert(insertQuery, st -> fillEntity(st, entity));
        Optional<T> optionalEntity = read(entityId);
        if (optionalEntity.isPresent()) {
            return optionalEntity.get();
        } else {
            throw new DaoException("Unable to create entity");
        }
    }

    @Override
    public List<T> read() throws DaoException {
        return executeStatement(selectAllQuery, this::extractResult);
    }

    @Override
    public Optional<T> read(Long id) throws DaoException {
        return executePreparedForEntity(selectByIdQuery, this::extractResult,
                st -> st.setLong(1, id));
    }

    @Override
    public boolean delete(Long id) throws DaoException {
        return executeUpdate(deleteById, st -> st.setLong(1, id)) > 0;
    }

    protected List<T> executePrepared(String sql, ResultSetExtractor<T> extractor,
                                      StatementPreparator statementPreparator) throws DaoException {
        try (final Connection conn = pool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparator.accept(statement);
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (SQLException | DatabaseConnectionException e) {
            logger.error("Unable to execute prepared statement", e);
            throw new DaoException(e);
        }
    }

    protected long executeInsert(String sql, StatementPreparator statementPreparator) throws DaoException {
        try (final Connection conn = pool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statementPreparator.accept(statement);
            statement.executeUpdate();
            final ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getLong(FIRST_COLUMN_INDEX);
        } catch (SQLException | DatabaseConnectionException e) {
            logger.error("could not execute insert", e);
            throw new DaoException(e);
        }
    }

    protected int executeUpdate(String sql, StatementPreparator statementPreparator) throws DaoException {
        try (final Connection conn = pool.takeConnection();
             final PreparedStatement statement = conn.prepareStatement(sql)) {
            statementPreparator.accept(statement);
            return statement.executeUpdate();
        } catch (SQLException | DatabaseConnectionException e) {
            logger.error("could not execute update", e);
            throw new DaoException(e);
        }
    }

    protected List<T> executeStatement(String sql, ResultSetExtractor<T> extractor) throws DaoException {
        try (final Connection conn = pool.takeConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (SQLException | DatabaseConnectionException e) {
            throw new DaoException(e);
        }
    }

    protected Optional<T> executePreparedForEntity(String sql, ResultSetExtractor<T> extractor,
                                                   StatementPreparator statementPreparator) throws DaoException {
        List<T> entities = executePrepared(sql, extractor, statementPreparator);
        return entities.isEmpty() ? Optional.empty() : Optional.of(entities.get(0));
    }

    protected abstract String getTableName();

    protected abstract List<String> getFields();

    protected abstract void fillEntity(PreparedStatement statement, T entity) throws SQLException;

    protected abstract T extractResult(ResultSet rs) throws DaoException;
}
