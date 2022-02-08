package com.epam.jwd.fitness_center.model.dao.impl;

import com.epam.jwd.fitness_center.exception.DaoException;
import com.epam.jwd.fitness_center.model.connection.ConnectionPool;
import com.epam.jwd.fitness_center.model.dao.BaseDao;
import com.epam.jwd.fitness_center.model.entity.Item;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ItemDaoImpl extends BaseDao<Item> {
    private static final Logger LOG = LogManager.getLogger(ItemDaoImpl.class);
    private static final String TABLE_NAME = "item";
    private static final String ID_FIELD_NAME = "id";
    private static final String NAME_FIELD_NAME = "name";
    private static final String PRICE_FIELD_NAME = "price";
    private static final String DESCRIPTION_FIELD_NAME = "description";
    private static final String ARCHIVE_FIELD_NAME = "archive";


    private static final List<String> TABLE_FIELDS = Arrays.asList(
            ID_FIELD_NAME, NAME_FIELD_NAME, PRICE_FIELD_NAME, DESCRIPTION_FIELD_NAME, ARCHIVE_FIELD_NAME
    );

    private static final String INSERT_NEW_ITEM_QUERY = "INSERT INTO " + TABLE_NAME +
            " (id, name, price, description, archive)\n" +
            "    VALUE (NULL, ?, ?, ?, DEFAULT)";

    private static final String UPDATE_QUERY_ADDITION = "name = ?, price = ?, description = ?, archive = ? ";

    ItemDaoImpl(ConnectionPool pool) {
        super(pool, LOG);
        updateQuery = String.format(updateQuery, UPDATE_QUERY_ADDITION);
        insertQuery = INSERT_NEW_ITEM_QUERY;
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected List<String> getFields() {
        return TABLE_FIELDS;
    }

    @Override
    protected void fillEntity(PreparedStatement statement, Item item) throws SQLException {
            statement.setString(1, item.getName());
            statement.setBigDecimal(2, item.getPrice());
            statement.setString(3, item.getDescription());
    }

    @Override
    protected Item extractResult(ResultSet rs) throws DaoException {
        try {
            return new Item(
                    rs.getLong(ID_FIELD_NAME),
                    rs.getString(NAME_FIELD_NAME),
                    rs.getBigDecimal(PRICE_FIELD_NAME),
                    rs.getString(DESCRIPTION_FIELD_NAME),
                    rs.getBoolean(ARCHIVE_FIELD_NAME));
        } catch (SQLException e) {
            throw new DaoException("Unable to extract item", e);
        }
    }

    @Override
    public boolean update(Item item) throws DaoException {
        int rows = executeUpdate(updateQuery, st -> {
            fillEntity(st, item);
            st.setBoolean(4, item.getIsArchive());
            st.setLong(5, item.getId());
        });
        return rows > 0;
    }
}
