package com.epam.jwd.fitness_center.dao;

import com.epam.jwd.fitness_center.db.ConnectionPool;
import com.epam.jwd.fitness_center.db.ConnectionPoolManager;
import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseDao<T extends Entity> implements EntityDao<T>{
    private static final Logger LOG = LogManager.getLogger(BaseDao.class);
    protected final ConnectionPool pool;

    protected BaseDao(ConnectionPool pool) {
        this.pool = pool;
    }

    protected List<T> executePrepared(String sql, ResultSetExtractor<T> extractor,
                                    StatementPreparator statementPreparator)  {
        try(final Connection conn = pool.takeConnection();
            final PreparedStatement statement = conn.prepareStatement(sql)) {
            if(statementPreparator != null) {
                statementPreparator.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (DaoException | SQLException | DatabaseConnectionException e) {
            LOG.error("Unable to execute prepared statement", e);
        }
        return Collections.emptyList();
    }

    protected List<T> executeStatement(String sql,ResultSetExtractor<T> extractor) throws DaoException {
        try(final Connection conn = pool.takeConnection();
            final Statement statement = conn.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (SQLException | DatabaseConnectionException e ) {
             throw new DaoException(e);
        }
    }

    @Override
    public T create(T entity) {
        return null;
    }

    @Override
    public List<T> read() throws DaoException {
            return executeStatement("SELECT * FROM " + getTableName(), null);

    }

    @Override
    public Optional<T> read(Long id) throws DaoException {
        return Optional.empty();
    }

    @Override
    public T update(T entity) throws DaoException {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    protected abstract String getTableName();
}
