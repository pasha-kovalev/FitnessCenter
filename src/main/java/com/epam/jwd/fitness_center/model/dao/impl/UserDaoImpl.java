package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


public class UserDaoImpl extends BaseDao<User> {
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);

    private static final String USER_TABLE_NAME = "user";
    private static final String ID_FIELD_NAME = "id";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String PASSWORD_HASH_FIELD_NAME = "password_hash";
    private static final String ACCOUNT_ROLE_FIELD_NAME = "account_role";
    private static final String STATUS_FIELD_NAME = "status";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";

    private static final List<String> USER_FIELDS = Arrays.asList(
            ID_FIELD_NAME, EMAIL_FIELD_NAME, PASSWORD_HASH_FIELD_NAME, ACCOUNT_ROLE_FIELD_NAME, STATUS_FIELD_NAME,
            FIRST_NAME_FIELD_NAME, SECOND_NAME_FIELD_NAME
    );

    private static final String SELECT_ALL_USERS = "SELECT  user.id,  email, password_hash, account_role, status,\n" +
            "first_name, second_name\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "ORDER BY second_name";


    private static final String SELECT_USERS_BY_ROLE = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = ?";

    private static final String SELECT_USER_BY_ID = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE user.id = ?";

    private static final String SELECT_ACTIVE_TRAINERS = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = 'trainer' AND status = 'active'";

    private static final String SELECT_USER_BY_EMAIL = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE email = ?";

    private static final String INSERT_NEW_USER = "INSERT INTO user (id, email, password_hash, role_id,\n" +
            "    user_status_id, first_name, second_name)\n" +
            "    VALUE (NULL, ?,?,\n" +
            "        (SELECT id FROM user_role WHERE account_role=?),\n" +
            "        (SELECT id FROM user_status WHERE status=?),\n" +
            "        ?,?)";

    private static final String UPDATE_USER_BY_ID = "UPDATE user\n" +
            "    SET email = ?, password_hash = ?,\n" +
            "    role_id = (SELECT id FROM user_role WHERE account_role = ?),\n" +
            "    user_status_id = (SELECT id FROM user_status WHERE status = ?),\n" +
            "    first_name = ?, second_name = ?\n" +
            "    WHERE user.id = ?";

    private static final String UPDATE_USER_STATUS_BY_ID = "UPDATE user\n" +
            "SET user_status_id = (SELECT id FROM user_status WHERE status = ?)\n" +
            "WHERE user.id = ?";

    //todo ? do them private and set
    {
        selectAllQuery = SELECT_ALL_USERS;
        insertQuery = INSERT_NEW_USER;
        selectByIdQuery = SELECT_USER_BY_ID;
        updateQuery = UPDATE_USER_BY_ID;
    }

    UserDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return USER_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return USER_FIELDS;
    }

    @Override
    protected void fillEntity(PreparedStatement statement, User entity) throws SQLException {
        statement.setString( 1,entity.getEmail());
        statement.setString( 2,entity.getPassword());
        statement.setString( 3,entity.getRole().toString().toLowerCase());
        statement.setString( 4,entity.getStatus().name().toLowerCase());
        statement.setString( 5,entity.getFirstName());
        statement.setString( 6,entity.getSecondName());
    }

    @Override
    public boolean update(User entity) throws DaoException {
        int rows =  executeUpdate(updateQuery, st -> {
            fillEntity(st, entity);
            st.setLong(7, entity.getId());
        });
        return rows > 0;
    }

    public boolean updateStatus(UserStatus status, long id) throws DaoException {
        int rows =  executeUpdate(UPDATE_USER_STATUS_BY_ID, st -> {
            st.setString(1, status.toString().toLowerCase());
            st.setLong(2, id);
        });
        return rows > 0;
    }

    public Optional<User> findByEmail(String email) throws DaoException {
        return executePreparedForEntity(SELECT_USER_BY_EMAIL, this::extractResult,
                st -> st.setString(1, email));
    }

    /*public Optional<User> findByRole(UserRole role) throws DaoException {
        return executePreparedForEntity(SELECT_USERS_BY_ROLE, this::extractResult,
                st -> st.setString(1, role.toString().toLowerCase()));
    }
*/
    public List<User> findActiveTrainers() throws DaoException {
        return executeStatement(SELECT_ACTIVE_TRAINERS, this::extractResult);
    }

    //todo make roleOf [instead of valueOf] like nmikle did
    @Override
    protected User extractResult(ResultSet rs) throws DaoException {
        try {
            return new User(
                    rs.getLong(ID_FIELD_NAME),
                    rs.getString(EMAIL_FIELD_NAME),
                    rs.getString(PASSWORD_HASH_FIELD_NAME),
                    rs.getString(FIRST_NAME_FIELD_NAME),
                    rs.getString(SECOND_NAME_FIELD_NAME),
                    UserRole.valueOf(rs.getString(ACCOUNT_ROLE_FIELD_NAME).toUpperCase(Locale.ENGLISH)),
                    UserStatus.valueOf(rs.getString(STATUS_FIELD_NAME).toUpperCase(Locale.ENGLISH))
            );
        } catch (SQLException e) {
            throw new DaoException("Unable to extract user", e);
        }
    }
}
