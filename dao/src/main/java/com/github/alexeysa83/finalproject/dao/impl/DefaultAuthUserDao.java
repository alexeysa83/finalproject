package com.github.alexeysa83.finalproject.dao.impl;

import com.github.alexeysa83.finalproject.dao.AuthUserDao;
import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DefaultAuthUserDao implements AuthUserDao {

    private MysqlConnection mysql = MysqlConnection.getInstance();

    private static volatile AuthUserDao instance;

    public static AuthUserDao getInstance() {
        AuthUserDao localInstance = instance;
        if (localInstance == null) {
            synchronized (AuthUserDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultAuthUserDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public AuthUser createAuthUser(String login, String password, String role) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("insert into auth_user (login, password, role) values (?, ?, ?)")) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.setString(3, role);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getAuthUserByLogin(login);
    }

    // Beautifull Role insertion, create in service

    @Override
    public AuthUser getAuthUserByLogin(String login) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select role, password from auth_user where login = ?")) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String r = resultSet.getString("role");
                    Role role = r.equalsIgnoreCase("admin") ? Role.ADMIN : Role.USER;
                    String password = resultSet.getString("password");
                    return new AuthUser(login, password, role);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean checkLoginIsTaken(String login) {
        boolean loginIsTaken = false;
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select role, password from auth_user where login = ?")) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                loginIsTaken = resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginIsTaken;
    }
}