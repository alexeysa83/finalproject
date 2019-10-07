package com.github.alexeysa83.finalproject.dao.impl;

import com.github.alexeysa83.finalproject.dao.AuthUserDao;
import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;

import java.sql.*;

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
    public AuthUser createAndSave(AuthUser user) {
        String login = user.getLogin();
        String password = user.getPassword();
        long id = 0;
        ResultSet rs = null;
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("insert into auth_user (login, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, login);
            statement.setString(2, password);
            statement.executeUpdate();
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return new AuthUser(id, login, password, Role.USER, false);
    }

//    @Override
//    public AuthUser getById(long id) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("select * from auth_user where id = ?")) {
//            statement.setLong(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    String login = resultSet.getString("login");
//                    String password = resultSet.getString("password");
//                    String r = resultSet.getString("role");
//                    Role role = Role.valueOf(r);
//                    boolean isBlocked = resultSet.getBoolean("is_blocked");
//                    return new AuthUser(id, login, password, role, isBlocked);
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public boolean update(AuthUser user) {
        boolean isUpdated = false;
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update auth_user set login = ?, password = ?, role = ? where id = ?")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.setLong(4, user.getId());
            isUpdated = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUpdated;
    }

    @Override
    public boolean delete(long id) {
        boolean isDeleted = false;
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update auth_user set is_blocked = true where id = ?")) {
            statement.setLong(1, id);
            isDeleted = statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isDeleted;
    }

    public AuthUser getByLogin(AuthUser user) {
        String login = user.getLogin();
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select * from auth_user where login = ?")) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String password = resultSet.getString("password");
                    String r = resultSet.getString("role");
                    Role role = Role.valueOf(r);
                    boolean isBlocked = resultSet.getBoolean("is_blocked");
                    return new AuthUser(id, login, password, role, isBlocked);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean checkLoginIsTaken(String login) {
        boolean loginIsTaken = false;
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select role, password from auth_user where login = ?")) {
            statement.setString(1, login);
            loginIsTaken = statement.executeQuery().next();
//            try (ResultSet resultSet = statement.executeQuery()) {
//                loginIsTaken = resultSet.next();
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loginIsTaken;
    }
}