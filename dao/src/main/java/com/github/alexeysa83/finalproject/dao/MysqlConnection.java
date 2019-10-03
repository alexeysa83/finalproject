package com.github.alexeysa83.finalproject.dao;

import java.sql.*;
import java.util.ResourceBundle;

public class MysqlConnection {

    private static volatile MysqlConnection instance;

    public static MysqlConnection getInstance() {
        MysqlConnection localInstance = instance;
        if (localInstance == null) {
            synchronized (MysqlConnection.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new MysqlConnection();
                }
            }
        }
        return localInstance;
    }

    public Connection getConnection() throws SQLException {
        ResourceBundle res = ResourceBundle.getBundle("database");
        String driver = res.getString("driver");
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        String url = res.getString("url");
        String user = res.getString("user");
        String password = res.getString("password");
        return DriverManager.getConnection(url, user, password);
    }
}
