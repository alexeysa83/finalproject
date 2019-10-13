package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserDao;
import com.github.alexeysa83.finalproject.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;

public class DefaultUserDao implements UserDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultUserDao.class);
    private MysqlConnection mysql = MysqlConnection.getInstance();

    private static volatile UserDao instance;

    public static UserDao getInstance() {
        UserDao localInstance = instance;
        if (localInstance == null) {
            synchronized (UserDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultUserDao();
                }
            }
        }
        return localInstance;
    }

    // Create and delete user logic in AuthUserDAO methods in transactions
    /*
    Get by auth_id method
     */
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
            log.error("SQLException at: {}", LocalDateTime.now(), e);
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
            log.info("User (auth_id): {} updated in DB at: {}", user.getAuthId(), LocalDateTime.now());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }
}
