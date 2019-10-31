package com.github.alexeysa83.finalproject.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class DataSource {

//    private static final Logger log = LoggerFactory.getLogger(DataSource.class);
//
//    private final ComboPooledDataSource pool;
//
//    public DataSource() {
//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        } catch (ClassNotFoundException e) {
//            log.error("Unable to load JDBC driver: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//        pool = new ComboPooledDataSource();
//        ResourceBundle resource = ResourceBundle.getBundle("database");
//        String url = resource.getString("url");
//        String user = resource.getString("user");
//        String pass = resource.getString("password");
//        pool.setJdbcUrl(url);
//        pool.setUser(user);
//        pool.setPassword(pass);
//
//        pool.setMinPoolSize(5);
//        pool.setAcquireIncrement(5);
//        pool.setMaxPoolSize(10);
//        pool.setMaxStatements(180);
//    }
//
//    private static volatile DataSource instance;
//
//    public static DataSource getInstance() {
//        DataSource localInstance = instance;
//        if (localInstance == null) {
//            synchronized (DataSource.class) {
//                localInstance = instance;
//                if (localInstance == null) {
//                    instance = localInstance = new DataSource();
//                }
//            }
//        }
//        return localInstance;
//    }
//
//    public Connection getConnection() {
//        try {
//            return this.pool.getConnection();
//        } catch (SQLException e) {
//            log.error("Unable to get connection to DB: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }
//
//    public Connection getConnection() throws SQLException {
//        ResourceBundle res = ResourceBundle.getBundle("database");
//        String driver = res.getString("driver");
//        try {
//            Class.forName(driver);
//        } catch (ClassNotFoundException e) {
//            log.error("Unable to load JDBC driver: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//        String url = res.getString("url");
//        String user = res.getString("user");
//        String password = res.getString("password");
//        return DriverManager.getConnection(url, user, password);
//    }
}
