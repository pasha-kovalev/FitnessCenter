package com.epam.jwd.fitness_center.model.connection;

import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;

import java.sql.Connection;

/** Interface of connection pool
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public interface ConnectionPool {

    /**Initialize connection pool
     * @return true if initialization possible
     */
    boolean init();

    /**Gets Is connection initialized
     * @return true if connection initialized
     */
    boolean isInitialized();

    /**Acquires connection
     * @return database connection
     * @throws DatabaseConnectionException is thrown if thread is interrupted
     */
    Connection takeConnection() throws DatabaseConnectionException;

    /**Returns connection back to connection pool
     * @param connection connection to be returned
     * @return true if connection returned successfully
     */
    boolean releaseConnection(Connection connection);

    /**Destroys pool, close all connections and stops daemon threads
     * @return true if pool is successfully shut down
     */
    boolean shutDown();
}
