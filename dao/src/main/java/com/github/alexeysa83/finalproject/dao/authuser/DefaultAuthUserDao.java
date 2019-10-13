package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.MysqlConnection;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;

public class DefaultAuthUserDao implements AuthUserDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultAuthUserDao.class);
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

    // default role and block status in DB or constructor???
    // Duplicate code
    // Check login method needed?

    @Override
    public AuthUser createAndSave(AuthUser user, Timestamp regTime) {
        Connection connection = null;
        final String login = user.getLogin();
        final String password = user.getPassword();
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement stateAuthUser = connection.prepareStatement
                    ("insert into auth_user (login, password) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stateUser = connection.prepareStatement
                         ("insert into user (registration_time, auth_id) values (?, ?)")) {
                stateAuthUser.setString(1, login);
                stateAuthUser.setString(2, password);
                stateAuthUser.executeUpdate();
                long id;
                try (ResultSet generatedKeys = stateAuthUser.getGeneratedKeys()) {
                    final boolean isSaved = generatedKeys.next();
                    if (!isSaved) {
                        return null;
                    }
                    id = generatedKeys.getLong(1);
                }
                stateUser.setTimestamp(1, regTime);
                stateUser.setLong(2, id);
                stateUser.executeUpdate();
                connection.commit();
                log.info("AuthUser id (User auth_id): {} saved to DB at: {}", id, LocalDateTime.now());
                return new AuthUser(id, login, password, Role.USER, false);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                log.error("Unable to save new user to DB with login: {}, password: {}, at: {}",
                        login, password, LocalDateTime.now());
            } catch (SQLException ex) {
                log.error("Unable to rollback transaction at: {}", LocalDateTime.now(), ex);
                throw new RuntimeException(ex);
            }
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Unable to close connection at: {}", LocalDateTime.now(), e);
                }
            }
        }
    }

    @Override
    public AuthUser getByLogin(String login) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select * from auth_user where login = ?")) {
            statement.setString(1, login);
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
                return new AuthUser(id, login, password, role, isBlocked);
            }
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
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
            log.error("SQLException at: {}", LocalDateTime.now(), e);
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
            log.info("AuthUser id: {} updated in DB at: {}", user.getId(), LocalDateTime.now());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(long id) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement stateAuthUser = connection.prepareStatement
                    ("update auth_user set is_blocked = true where id = ?");
                 PreparedStatement stateUser = connection.prepareStatement
                         ("delete from user where auth_id = ?")) {
                stateAuthUser.setLong(1, id);
                final int i = stateAuthUser.executeUpdate();
                stateUser.setLong(1, id);
                int b = stateUser.executeUpdate();
                connection.commit();
                log.info("AuthUser id (User auth_id): {} deleted from DB at: {}", id, LocalDateTime.now());
                return i > 0 && b > 0;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                log.error("Unable to delete AuthUser id: {} from DB, at: {}", id, LocalDateTime.now());
            } catch (SQLException ex) {
                log.error("Unable to rollback transaction at: {}", LocalDateTime.now(), ex);
                throw new RuntimeException(ex);
            }
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Unable to close connection at: {}", LocalDateTime.now(), e);
                }
            }
        }
    }

//    public boolean checkLoginIsTaken(String login) {
//        try (Connection connection = mysql.getConnection();
//             PreparedStatement statement = connection.prepareStatement
//                     ("select role, password from auth_user where login = ?")) {
//            statement.setString(1, login);
//            return statement.executeQuery().next();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//    }
}