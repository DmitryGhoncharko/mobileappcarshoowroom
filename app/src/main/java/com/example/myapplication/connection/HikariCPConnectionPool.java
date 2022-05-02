package com.example.myapplication.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.ProxyConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class HikariCPConnectionPool implements ConnectionPool{
    private static final Logger LOG = LoggerFactory.getLogger(HikariCPConnectionPool.class);
    private static final HikariConfig CONFIG = new HikariConfig();
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_DATABASE_FILE_NAME = "database.properties";
    private static final String PROPERTY_URL = "db.url";
    private static final String PROPERTY_USER = "db.user";
    private static final String PROPERTY_PASSWORD = "db.password";
    private static final String PROPERTY_MAX_POOL_SIZE = "db.maxpoolsize";
    private static final String PROPERTY_DRIVER = "db.driver";
    private static final HikariDataSource ds;

    static {

        CONFIG.setJdbcUrl("jdbc:mysql://localhost:3306/carshowroom");
        CONFIG.setUsername("carshowroom");
        CONFIG.setPassword("jmXzj3eV#");
        CONFIG.setDriverClassName("com.mysql.cj.jdbc.Driver");
        CONFIG.setMaximumPoolSize(Integer.parseInt("6"));
        ds = new HikariDataSource(CONFIG);
        LOG.info("Database connected successful");
    }

    @Override
    public ProxyConnection getConnection() throws SQLException {
        return (ProxyConnection) ds.getConnection();
    }
}
