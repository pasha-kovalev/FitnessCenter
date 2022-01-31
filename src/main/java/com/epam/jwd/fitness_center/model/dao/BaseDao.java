package com.epam.jwd.fitness_center.model.dao;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.entity.Entity;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**Dao abstract class with base methods and queries
 * @param <T> entity type
 */
public abstract class BaseDao<T extends Entity> implements EntityDao<T> {
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

    protected String insertQuery;
    protected String updateQuery;
    protected String selectAllQuery;
    protected String selectByIdQuery;
    private final String deleteById;

    private final Logger logger;

    {
        insertQuery = String.format(INSERT_INTO, getTableName(), String.join(COMMA, getFields()));
        selectAllQuery = String.format(SELECT_ALL_FROM, String.join(COMMA, getFields())) + getTableName();
        selectByIdQuery = selectAllQuery + SPACE + WHERE_ID;
        updateQuery = UPDATE + getTableName() + SPACE + SET + "%s" + WHERE_ID;
        deleteById = DELETE_FROM + getTableName() + SPACE + WHERE_ID;
    }

    protected BaseDao(ConnectionPool pool, Logger logger) {
        this.pool = pool;
        this.logger = logger;
    }

    /**Inserts new entity
     * @param entity entity
     * @return inserted entity
     * @throws DaoException when error while query execution occurs or cannot insert new entity
     */
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

    /**Selects all entities
     *
     * @return list of found entities
     * @throws DaoException when error while query execution occurs
     */
    @Override
    public List<T> read() throws DaoException {
        return executeStatement(selectAllQuery, this::extractResult);
    }

    /**Finds entity by id
     *
     * @param id entity id
     * @return optional with entity or empty optional
     * @throws DaoException when error while query execution occurs
     */
    @Override
    public Optional<T> read(Long id) throws DaoException {
        return executePreparedForEntity(selectByIdQuery, this::extractResult,
                st -> st.setLong(1, id));
    }

    /**Deletes entity by id
     * @param id entity id
     * @return true if deleting was successful
     * @throws DaoException when error while query execution occurs
     */
    @Override
    public boolean delete(Long id) throws DaoException {
        return executeUpdate(deleteById, st -> st.setLong(1, id)) > 0;
    }

    /**Executes sql query using prepared statement
     * @param sql sql query
     * @param extractor extractor of result set
     * @param statementPreparator preparator for statement
     * @return list of found entities or empty list
     * @throws DaoException when error while query execution occurs
     */
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

    /**Executes sql insert query using prepared statement
     * @param sql sql query
     * @param statementPreparator preparator for statement
     * @return id of entity in db
     * @throws DaoException when error while query execution occurs
     */
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

    /**Executes sql update query using prepared statement
     * @param sql sql query
     * @param statementPreparator preparator for statement
     * @return either the row count or 0 for SQL statements that return nothing
     * @throws DaoException when error while query execution occurs
     */
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

    /**Executes sql query using statement
     * @param sql sql query
     * @param extractor extractor of result set
     * @return list of found entities or empty list
     * @throws DaoException when error while query execution occurs
     */
    protected List<T> executeStatement(String sql, ResultSetExtractor<T> extractor) throws DaoException {
        try (final Connection conn = pool.takeConnection();
             final Statement statement = conn.createStatement();
             final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (SQLException | DatabaseConnectionException e) {
            throw new DaoException(e);
        }
    }

    /**Executes sql query using prepared statement when one return entity is expected
     * @param sql sql query
     * @param extractor extractor of result set
     * @param statementPreparator preparator for statement
     * @return returns optional with entity or empty optional
     * @throws DaoException when error while query execution occurs
     */
    protected Optional<T> executePreparedForEntity(String sql, ResultSetExtractor<T> extractor,
                                                   StatementPreparator statementPreparator) throws DaoException {
        List<T> entities = executePrepared(sql, extractor, statementPreparator);
        return entities.isEmpty() ? Optional.empty() : Optional.of(entities.get(0));
    }

    /**Gets the table name
     * @return table name
     */
    protected abstract String getTableName();

    /**Gets the table fields
     * @return list of fields
     */
    protected abstract List<String> getFields();

    /**Retrieves values from a result set into an entity
     * @param rs result set
     * @return entity
     * @throws DaoException when error while query execution occurs
     */
    protected abstract T extractResult(ResultSet rs) throws DaoException;

    /**Fills the statement with values from given entity
     * @param statement prepared statement
     * @param entity entity
     * @throws SQLException if parameterIndex does not correspond to a parameter marker in the SQL statement;
     * if a database access error occurs or this method is called on a closed PreparedStatement
     */
    protected abstract void fillEntity(PreparedStatement statement, T entity) throws SQLException;
}
