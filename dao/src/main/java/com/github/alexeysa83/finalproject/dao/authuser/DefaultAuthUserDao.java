package com.github.alexeysa83.finalproject.dao.authuser;

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

    // default role and block in DB or constructor
    @Override
    public AuthUser createAndSave(AuthUser user) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("insert into auth_user (login, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                final boolean isSaved = generatedKeys.next();
                if (!isSaved) {
                    return null;
                }
                final long id = generatedKeys.getLong(1);
                return new AuthUser(id, user.getLogin(), user.getPassword(), Role.USER, false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthUser getById(long id) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select * from auth_user where id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                final boolean exist = resultSet.next();
                if (!exist) {
                    return null;
                }
                final String login = resultSet.getString("login");
                final String password = resultSet.getString("password");
                final String r = resultSet.getString("role");
                final Role role = Role.valueOf(r);
                final boolean isBlocked = resultSet.getBoolean("is_blocked");
                return new AuthUser(id, login, password, role, isBlocked);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(AuthUser user) {

        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update auth_user set login = ?, password = ?, role = ? where id = ?")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());
            statement.setLong(4, user.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update auth_user set is_blocked = true where id = ?")) {
            statement.setLong(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Duplicate code???
    public AuthUser getByLogin(AuthUser user) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select * from auth_user where login = ?")) {
            statement.setString(1, user.getLogin());
            try (ResultSet resultSet = statement.executeQuery()) {
                final boolean exist = resultSet.next();
                if (!exist) {
                    return null;
                }
                final long id = resultSet.getLong("id");
                final String password = resultSet.getString("password");
                final String r = resultSet.getString("role");
                final Role role = Role.valueOf(r);
                boolean isBlocked = resultSet.getBoolean("is_blocked");
                return new AuthUser(id, user.getLogin(), password, role, isBlocked);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public boolean checkLoginIsTaken(String login) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select role, password from auth_user where login = ?")) {
            statement.setString(1, login);
            return statement.executeQuery().next();
//            try (ResultSet resultSet = statement.executeQuery()) {
//                loginIsTaken = resultSet.next();
//            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}