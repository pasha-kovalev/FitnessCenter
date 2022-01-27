package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.exception.ServiceException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.dao.OrderDao;
import com.epam.jwd.fitness_center.model.entity.Order;
import com.epam.jwd.fitness_center.model.entity.OrderStatus;
import com.epam.jwd.fitness_center.model.service.impl.ServiceProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

public class OrderDaoImpl extends BaseDao<Order> implements OrderDao {
    private static final Logger LOG = LogManager.getLogger(OrderDaoImpl.class);
    private static final String ORDER_TABLE_NAME = "order";
    private static final String ID_FIELD_NAME = "id";
    private static final String USER_DETAILS_ID_FIELD_NAME = "user_details_id";
    private static final String ORDER_STATUS_FIELD_NAME = "order_status";
    private static final String ITEM_ID_FIELD_NAME = "item_id";
    private static final String ASSIGNMENT_TRAINER_ID_FIELD_NAME = "assignment_trainer_id";
    private static final String TRAINER_ID_FIELD_NAME = "trainer_id";
    private static final String TRAINER_NAME_FIELD_NAME = "trainer_name";
    private static final String PRICE_FIELD_NAME = "price";
    private static final String COMMENT_FIELD_NAME = "comment";
    private static final String CREATED_AT_FIELD_NAME = "created_at";
    private static final String REVIEW_FIELD_NAME = "review";
    private static final String PERIOD_FIELD_NAME = "period";

    private static final List<String> ORDER_FIELDS = Arrays.asList(
            ID_FIELD_NAME, USER_DETAILS_ID_FIELD_NAME, ORDER_STATUS_FIELD_NAME, ITEM_ID_FIELD_NAME,
            ASSIGNMENT_TRAINER_ID_FIELD_NAME, TRAINER_ID_FIELD_NAME,
            PRICE_FIELD_NAME, COMMENT_FIELD_NAME, CREATED_AT_FIELD_NAME, REVIEW_FIELD_NAME, PERIOD_FIELD_NAME
    );

    private static final String SELECT_ALL_ORDERS = "SELECT order.id, user_details_id, order_status, item_id,\n" +
            " assignment_trainer_id, trainer_id, second_name as trainer_name, price, comment, created_at, review, period\n" +
            "FROM `order`\n" +
            "JOIN order_status s on s.id = order.order_status_id\n" +
            "JOIN user u on u.id = order.trainer_id";

    private static final String SELECT_ORDER_BY_ID = "SELECT order.id, user_details_id, order_status, item_id,\n" +
            "       assignment_trainer_id, trainer_id, second_name as trainer_name, price, comment, created_at," +
            " review, period\n" +
            "FROM `order`\n" +
            "JOIN order_status s on s.id = order.order_status_id\n" +
            "JOIN user u on u.id = order.trainer_id\n" +
            "WHERE `order`.id = ?";

    private static final String SELECT_ORDER_BY_STATUS = "SELECT order.id, user_details_id, order_status, item_id,\n" +
            "       assignment_trainer_id, trainer_id, second_name as trainer_name, price, comment, created_at, " +
            "review, period\n" +
            "FROM `order`\n" +
            "JOIN order_status s on s.id = order.order_status_id\n" +
            "JOIN user u on u.id = order.trainer_id\n" +
            "WHERE order_status = ?";

    private static final String SELECT_ORDER_BY_TRAINER_ID = "SELECT order.id, user_details_id, order_status, \n" +
            "item_id, assignment_trainer_id, trainer_id, second_name as trainer_name, price, comment, " +
            "created_at, review, period\n" +
            "FROM `order`\n" +
            "JOIN order_status s on s.id = order.order_status_id\n" +
            "JOIN user u on u.id = order.trainer_id\n" +
            "WHERE trainer_id = ?";

    private static final String SELECT_ORDER_BY_USER_ID = "SELECT order.id, user_details_id, order_status, \n" +
            "item_id, assignment_trainer_id, trainer_id, second_name as trainer_name, price, comment, " +
            "created_at, review, period\n" +
            "FROM `order`\n" +
            "JOIN order_status s on s.id = order.order_status_id\n" +
            "JOIN user u on u.id = order.trainer_id\n" +
            "WHERE user_details_id = ?";

    private static final String SELECT_ORDER_BY_ASSIGNMENT_TRAINER_ID = "SELECT order.id, user_details_id, \n" +
            "order_status, item_id, assignment_trainer_id, trainer_id, second_name as trainer_name, price, " +
            "comment, created_at, review, period\n" +
            "FROM `order`\n" +
            "JOIN order_status s on s.id = order.order_status_id\n" +
            "JOIN user u on u.id = order.trainer_id\n" +
            "WHERE assignment_trainer_id = ?";

    private static final String INSERT_NEW_ORDER = "insert into `order` (id, user_details_id, order_status_id, " +
            "item_id, assignment_trainer_id, trainer_id, price, comment,\n" +
            "created_at, review, period)\n" +
            "value (null, ?,\n" +
            "       (SELECT id FROM order_status WHERE order_status=?),\n" +
            "      ?, ?, ?, ?, ?, default, ?, ?);";

    private static final String UPDATE_ORDER_BY_ID = "update `order`\n" +
            "set user_details_id = ?,\n" +
            "    order_status_id = (SELECT id FROM order_status WHERE order_status=?),\n" +
            "    item_id = ?, assignment_trainer_id = ?, trainer_id = ?, price = ?,\n" +
            "    comment = ?, review = ?, period = ?\n" +
            "where `order`.id = ?";

    private static final String UPDATE_ORDER_STATUS_BY_ID = "UPDATE `order`\n" +
            "SET order_status_id = (SELECT id FROM order_status WHERE order_status=?)\n" +
            "WHERE `order`.id = ?";

    {
        selectAllQuery = SELECT_ALL_ORDERS;
        insertQuery = INSERT_NEW_ORDER;
        selectByIdQuery = SELECT_ORDER_BY_ID;
        updateQuery = UPDATE_ORDER_BY_ID;
    }

    OrderDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
    }

    @Override
    protected String getTableName() {
        return ORDER_TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return ORDER_FIELDS;
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Order entity) throws SQLException {
        statement.setLong(1, entity.getUserDetailsId());
        statement.setString(2, entity.getOrderStatus().name().toLowerCase());
        statement.setLong(3, entity.getItem().getId());
        if (entity.getAssignmentTrainerId() == null) {
            statement.setNull(4, Types.INTEGER);
        } else {
            statement.setLong(4, entity.getAssignmentTrainerId());
        }
        if (entity.getTrainerId() == null) {
            statement.setNull(5, Types.INTEGER);
        } else {
            statement.setLong(5, entity.getTrainerId());
        }
        statement.setBigDecimal(6, entity.getPrice());
        statement.setString(7, entity.getComment());
        if (entity.getReview() == null) {
            statement.setNull(8, Types.VARCHAR);
        } else {
            statement.setString(8, entity.getReview());
        }
        statement.setLong(9, entity.getPeriod());
    }

    @Override
    protected Order extractResult(ResultSet rs) throws DaoException {
        try {
            return new Order.Builder()
                    .setId(rs.getLong(ID_FIELD_NAME))
                    .setUserDetailsId(rs.getLong(USER_DETAILS_ID_FIELD_NAME))
                    .setOrderStatus(OrderStatus.valueOf(rs.getString(ORDER_STATUS_FIELD_NAME).toUpperCase()))
                    .setItem(ServiceProvider.getInstance()
                            .getItemService()
                            .find(rs.getLong(ITEM_ID_FIELD_NAME))
                            .orElseThrow(() -> new DaoException("Unable to set item in order")))
                    .setAssignmentTrainerId(rs.getLong(ASSIGNMENT_TRAINER_ID_FIELD_NAME))
                    .setTrainerId(rs.getLong(TRAINER_ID_FIELD_NAME))
                    .setTrainerName(rs.getString(TRAINER_NAME_FIELD_NAME))
                    .setPrice(rs.getBigDecimal(PRICE_FIELD_NAME))
                    .setComment(rs.getString(COMMENT_FIELD_NAME))
                    .setCreationDate(rs.getTimestamp(CREATED_AT_FIELD_NAME).toLocalDateTime())
                    .setReview(rs.getString(REVIEW_FIELD_NAME))
                    .setPeriod(rs.getLong(PERIOD_FIELD_NAME))
                    .build();
        } catch (SQLException | ServiceException e) {
            throw new DaoException("Unable to extract order", e);
        }
    }

    @Override
    public boolean update(Order entity) throws DaoException {
        int rows = executeUpdate(updateQuery, st -> {
            fillEntity(st, entity);
            st.setLong(10, entity.getId());
        });
        return rows > 0;
    }

    @Override
    public boolean updateStatus(OrderStatus status, long id) throws DaoException {
        int rows = executeUpdate(UPDATE_ORDER_STATUS_BY_ID, st -> {
            st.setString(1, status.name().toLowerCase());
            st.setLong(2, id);
        });
        return rows > 0;
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) throws DaoException {
        return executePrepared(SELECT_ORDER_BY_STATUS, this::extractResult,
                st -> st.setString(1, status.name().toLowerCase()));
    }

    @Override
    public List<Order> findByTrainerId(Long trainerId) throws DaoException {
        return executePrepared(SELECT_ORDER_BY_TRAINER_ID, this::extractResult,
                st -> st.setLong(1, trainerId));
    }

    @Override
    public List<Order> findByUserId(Long userId) throws DaoException {
        return executePrepared(SELECT_ORDER_BY_USER_ID, this::extractResult,
                st -> st.setLong(1, userId));
    }

    @Override
    public List<Order> findByAssignmentTrainerId(Long trainerId) throws DaoException {
        return executePrepared(SELECT_ORDER_BY_ASSIGNMENT_TRAINER_ID, this::extractResult,
                st -> st.setLong(1, trainerId));
    }


}
