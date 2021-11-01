package com.epam.jwd.fitness_center.db;

import com.epam.jwd.fitness_center.exception.DatabaseConnectionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class ConnectionFactory {
    private static final Logger LOG = LogManager.getLogger(ConnectionFactory.class);

    private static final String DATABASE_CONFIG_PATH = "database/database.properties";
    private static final String DB_URL;

    private static final Properties databaseProperties;

    static {
        try(InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream(DATABASE_CONFIG_PATH)) {
            if(is == null) {
                LOG.fatal("Unable to find data base property file");
                throw new RuntimeException("Unable to find data base property file");
            }
            databaseProperties =  new Properties();
            databaseProperties.load(is);
            LOG.info("Data base property file loaded");
        }  catch (IOException e) {
            LOG.fatal("Unable to open data base property file", e);
            throw new RuntimeException("Unable to open data base property file");
        }
        DB_URL = databaseProperties.getProperty("url");

        if(DB_URL == null) {
            LOG.fatal("Database url in configuration file is not correct");
            throw new RuntimeException("Database configuration file is not correct");
        }
    }

    private ConnectionFactory() {
    }

    static ProxyConnection createProxyConnection() throws DatabaseConnectionException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(DB_URL, databaseProperties);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Unable to create connection to database: " + DB_URL);
        }
        return new ProxyConnection(connection);
    }
}
