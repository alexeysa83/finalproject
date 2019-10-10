package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.model.User;

import java.sql.*;

public class DefaultUserDAO implements UserDAO {

    private MysqlConnection mysql = MysqlConnection.getInstance();

    private static volatile UserDAO instance;

    public static UserDAO getInstance() {
        UserDAO localInstance = instance;
        if (localInstance == null) {
            synchronized (UserDAO.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultUserDAO();
                }
            }
        }
        return localInstance;
    }

    // Create and delete user logic in AuthUserDAO methods in transactions
    @Override
    public User getById(long authId) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select u.id, u.first_name, u.last_name, u.registration_time, u.email, u.phone, au.login " +
                             "from user u join auth_user au on u.auth_id = au.id where u.auth_id = ?")) {
            statement.setLong(1, authId);
            try (ResultSet resultSet = statement.executeQuery()) {
                final boolean exist = resultSet.next();
                if (!exist) {
                    return null;
                }
                final long id = resultSet.getLong("id");
                final String firstName = resultSet.getString("first_name");
                final String lastName = resultSet.getString("last_name");
                final Timestamp registrationTime = resultSet.getTimestamp("registration_time");
                final String email = resultSet.getString("email");
                final String phone = resultSet.getString("phone");
                final String login = resultSet.getString("login");
                return new User(id, firstName, lastName, registrationTime, email, phone, authId, login);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean update(User user) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update user set first_name = ?, last_name = ?, email = ?, phone = ? where auth_id = ?")) {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPhone());
            statement.setLong(5, user.getAuthId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
