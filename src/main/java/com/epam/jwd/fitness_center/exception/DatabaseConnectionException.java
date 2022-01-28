package com.epam.jwd.fitness_center.exception;

/**The class represents database connection exception
 *
 * @author Pavel Kovalev
 * @version 1.0
 */
public class DatabaseConnectionException extends Exception {
    public DatabaseConnectionException() {
    }

    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseConnectionException(Throwable cause) {
        super(cause);
    }
}
