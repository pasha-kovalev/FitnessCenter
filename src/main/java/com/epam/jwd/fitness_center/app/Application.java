package com.epam.jwd.fitness_center.app;

import com.epam.jwd.fitness_center.dao.*;
import com.epam.jwd.fitness_center.db.ConnectionPoolManager;
import com.epam.jwd.fitness_center.exception.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Collections;
import java.util.List;

public class Application {
    private static final Logger LOG = LogManager.getLogger(Application.class);

    public static final String SELECT_ALL_USERS = "SELECT  user.id,  email, second_name, first_name, password_hash, account_role, status\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "ORDER BY second_name";

    public static final String SELECT_CORPORATE_USERS = "SELECT  user.id,  email, second_name, first_name, password_hash, account_role, status\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = 'corporate_user'";

    public static final String SELECT_REGULAR_USERS = "SELECT  user.id,  email, second_name, first_name, password_hash, account_role, status\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = 'regular_user'";

    public static final String SELECT_ACTIVE_TRAINERS = "SELECT  user.id,  email, second_name, first_name, password_hash, account_role, status\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = 'trainer' AND status = 'active'";
    public static final String SELECT_USER_BY_ID = "SELECT  user.id,  email, second_name, first_name, password_hash, account_role, status\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE user.id = ?";
    public static final String SELECT_USER_BY_EMAIL = "SELECT  user.id,  email, second_name, first_name, password_hash, account_role, status\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE email = ?";
    public static final String INSERT_NEW_USER = "INSERT INTO user (id, email, password_hash, first_name, second_name, role_id, user_status_id)\n" +
            "VALUE (NULL, ?,?,?,?,\n" +
            "        (SELECT id FROM user_role WHERE account_role=?),\n" +
            "        (SELECT id FROM user_status WHERE status=?))";

    public static final String UPDATE_USER_BY_ID = "UPDATE user\n" +
            "SET email = ?, second_name = ?, first_name = ?, password_hash = ?,\n" +
            "    role_id = (SELECT id FROM user_role WHERE account_role = ?),\n" +
            "    user_status_id = (SELECT id FROM user_status WHERE status = ?)\n" +
            "WHERE user.id = ?";

    public static final String UPDATE_USER_STATUS_BY_ID = "UPDATE user\n" +
            "SET user_status_id = (SELECT id FROM user_status WHERE status = ?)\n" +
            "WHERE user.id = ?";


    public static void main(String[] args) {
        try {
            ConnectionPoolManager.getInstance().init();
            List<User> users = fetchUsersFromDb();
            users.forEach(System.out::println);
        } finally {
            ConnectionPoolManager.getInstance().shutDown();
        }

    }

    //fixme magic values to constants
    private static User extractUser(ResultSet resultSet) throws DaoException {
        try {
            return new User(
                    (long) resultSet.getInt("id"),
                    resultSet.getString("email"),
                    resultSet.getString("password_hash"),
                    resultSet.getString("first_name"),
                    resultSet.getString("second_name"),
                    UserRole.valueOf(resultSet.getString("account_role").toUpperCase()),
                    UserStatus.valueOf(resultSet.getString("status").toUpperCase())
            );
        } catch (SQLException e) {
            throw new DaoException("Unable to extract user", e);
        }
    }

    private static List<User> fetchUsersFromDb() {
        return executeStatement(SELECT_ALL_USERS, Application::extractUser);
    }

    private static <T extends Entity> List<T> executePrepared(String sql,
                                                              ResultSetExtractor<T> extractor,
                                                              StatementPreparator statementPreparator)  {
        try(final Connection conn = ConnectionPoolManager.getInstance().takeConnection();
            final PreparedStatement statement = conn.prepareStatement(sql)) {
            if(statementPreparator != null) {
                statementPreparator.accept(statement);
            }
            final ResultSet resultSet = statement.executeQuery();
            return extractor.extractAll(resultSet);
        } catch (DaoException | SQLException e) {
            LOG.error("Unable to execute prepared statement", e);
        }
        return Collections.emptyList();
    }

    private static <T extends Entity> List<T> executeStatement(String sql,
                                                              ResultSetExtractor<T> extractor) {
        try(final Connection conn = ConnectionPoolManager.getInstance().takeConnection();
            final Statement statement = conn.createStatement();
            final ResultSet resultSet = statement.executeQuery(sql)) {
            return extractor.extractAll(resultSet);
        } catch (DaoException | SQLException e) {
            LOG.error("Unable to execute statement", e);
        }
        return Collections.emptyList();
    }
}
