package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.dao.UserDao;
import com.epam.jwd.fitness_center.model.entity.User;
import com.epam.jwd.fitness_center.model.entity.UserRole;
import com.epam.jwd.fitness_center.model.entity.UserStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class UserDaoImpl extends BaseDao<User> implements UserDao {
    private static final Logger LOG = LogManager.getLogger(UserDaoImpl.class);
    private static final String USER_TABLE_NAME = "user";
    private static final String ID_FIELD_NAME = "id";
    private static final String EMAIL_FIELD_NAME = "email";
    private static final String PASSWORD_HASH_FIELD_NAME = "password_hash";
    private static final String ACCOUNT_ROLE_FIELD_NAME = "account_role";
    private static final String STATUS_FIELD_NAME = "status";
    private static final String FIRST_NAME_FIELD_NAME = "first_name";
    private static final String SECOND_NAME_FIELD_NAME = "second_name";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String PHOTO_PATH_FIELD_NAME = "photo_path";


    private static final List<String> USER_FIELDS = Arrays.asList(
            ID_FIELD_NAME, EMAIL_FIELD_NAME, PASSWORD_HASH_FIELD_NAME, ACCOUNT_ROLE_FIELD_NAME, STATUS_FIELD_NAME,
            FIRST_NAME_FIELD_NAME, SECOND_NAME_FIELD_NAME, DESCRIPTION_FIELD_NAME, PHOTO_PATH_FIELD_NAME
    );

    private static final String SELECT_ALL_USERS = "SELECT  user.id,  email, password_hash, account_role, status,\n" +
            "first_name, second_name, description, photo_path\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "ORDER BY second_name";


    private static final String SELECT_USERS_BY_ROLE = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name, description, photo_path\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = ?";

    private static final String SELECT_USER_BY_ID = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name, description, photo_path\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE user.id = ?";

    private static final String SELECT_ACTIVE_TRAINERS = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name, description, photo_path\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE account_role = 'trainer' AND status = 'active'";

    private static final String SELECT_ACTIVE_CLIENTS = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name, description, photo_path\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE (account_role = 'user' OR account_role = 'corporate_user' OR account_role = 'regular_user') " +
            "      AND status = 'active'";

    private static final String SELECT_USER_BY_EMAIL = "SELECT user.id,  email, password_hash, account_role,\n" +
            "status, first_name, second_name, description, photo_path\n" +
            "FROM user\n" +
            "JOIN user_role r on r.id = user.role_id\n" +
            "JOIN user_status us on us.id = user.user_status_id\n" +
            "WHERE email = ?";

    private static final String INSERT_NEW_USER = "INSERT INTO user (id, email, password_hash, role_id,\n" +
            "    user_status_id, first_name, second_name, description, photo_path)\n" +
            "    VALUE (NULL, ?,?,\n" +
            "        (SELECT id FROM user_role WHERE account_role=?),\n" +
            "        (SELECT id FROM user_status WHERE status=?),\n" +
            "        ?,?, ?, ?)";


    private static final String UPDATE_USER_BY_ID = "UPDATE user\n" +
            "    SET email = ?, password_hash = ?,\n" +
            "    role_id = (SELECT id FROM user_role WHERE account_role = ?),\n" +
            "    user_status_id = (SELECT id FROM user_status WHERE status = ?),\n" +
            "    first_name = ?, second_name = ?, description = ?, photo_path = ?\n" +
            "    WHERE user.id = ?";

    private static final String UPDATE_USER_STATUS_BY_ID = "UPDATE user\n" +
            "SET user_status_id = (SELECT id FROM user_status WHERE status = ?)\n" +
            "WHERE user.id = ?";

    private static final String UPDATE_USER_ROLE_BY_ID = "UPDATE user\n" +
            "SET role_id = (SELECT id FROM user_role WHERE account_role = ?)\n" +
            "WHERE user.id = ?";


    UserDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
        selectAllQuery = SELECT_ALL_USERS;
        insertQuery = INSERT_NEW_USER;
        selectByIdQuery = SELECT_USER_BY_ID;
        updateQuery = UPDATE_USER_BY_ID;
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
        statement.setString(1, entity.getEmail());
        statement.setString(2, entity.getPassword());
        statement.setString(3, entity.getRole().name().toLowerCase());
        statement.setString(4, entity.getStatus().name().toLowerCase());
        statement.setString(5, entity.getFirstName());
        statement.setString(6, entity.getSecondName());
        if (entity.getDescription() == null) {
            statement.setNull(7, Types.VARCHAR);
        } else {
            statement.setString(7, entity.getDescription());
        }
        if (entity.getPhotoPath() == null) {
            statement.setNull(8, Types.VARCHAR);
        } else {
            statement.setString(8, entity.getPhotoPath());
        }
    }

    @Override
    public boolean update(User entity) throws DaoException {
        int rows = executeUpdate(updateQuery, st -> {
            fillEntity(st, entity);
            st.setLong(9, entity.getId());
        });
        return rows > 0;
    }

    @Override
    public boolean updateStatus(UserStatus status, long id) throws DaoException {
        int rows = executeUpdate(UPDATE_USER_STATUS_BY_ID, st -> {
            st.setString(1, status.name().toLowerCase());
            st.setLong(2, id);
        });
        return rows > 0;
    }

    @Override
    public boolean updateRole(UserRole role, long id) throws DaoException {
        int rows = executeUpdate(UPDATE_USER_ROLE_BY_ID, st -> {
            st.setString(1, role.name().toLowerCase());
            st.setLong(2, id);
        });
        return rows > 0;
    }

    @Override
    public Optional<User> findByEmail(String email) throws DaoException {
        return executePreparedForEntity(SELECT_USER_BY_EMAIL, this::extractResult,
                st -> st.setString(1, email));
    }

    @Override
    public List<User> findActiveTrainers() throws DaoException {
        return executeStatement(SELECT_ACTIVE_TRAINERS, this::extractResult);
    }

    @Override
    public List<User> findActiveClients() throws DaoException {
        return executeStatement(SELECT_ACTIVE_CLIENTS, this::extractResult);
    }

    @Override
    protected User extractResult(ResultSet rs) throws DaoException {
        try {
            return new User.Builder()
                    .setId(rs.getLong(ID_FIELD_NAME))
                    .setEmail(rs.getString(EMAIL_FIELD_NAME))
                    .setPassword(rs.getString(PASSWORD_HASH_FIELD_NAME))
                    .setFirstName(rs.getString(FIRST_NAME_FIELD_NAME))
                    .setRole(UserRole.of(rs.getString(ACCOUNT_ROLE_FIELD_NAME)))
                    .setSecondName(rs.getString(SECOND_NAME_FIELD_NAME))
                    .setStatus(UserStatus.of(rs.getString(STATUS_FIELD_NAME)))
                    .setDescription(rs.getString(DESCRIPTION_FIELD_NAME))
                    .setPhotoPath(rs.getString(PHOTO_PATH_FIELD_NAME))
                    .build();
        } catch (SQLException e) {
            throw new DaoException("Unable to extract user", e);
        }
    }
}
