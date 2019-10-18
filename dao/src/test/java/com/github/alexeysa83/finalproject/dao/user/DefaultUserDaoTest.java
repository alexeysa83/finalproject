package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DefaultUserDaoTest {

    private final UserBaseDao userDAO = DefaultUserBaseDao.getInstance();
    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private DataSource mysql = DataSource.getInstance();

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    @Test
    void getInstance() {
        assertNotNull(userDAO);
    }

    @Test
    void getById() {
        final AuthUser authUser = new AuthUser("UserID", "Test");
        final Timestamp regTime = getTime();
        final AuthUser savedAuthUser = authUserDao.createAndSave(authUser, regTime);
        final long authId = savedAuthUser.getId();

        // Testing transaction's second insert into user table
        final User testUser = userDAO.getById(authId);
        assertNotNull(testUser);

        final Long userId = testUser.getId();
        assertNotNull(userId);
        assertEquals(regTime, testUser.getRegistrationTime());
        assertEquals(authId, testUser.getAuthId());
        assertEquals(savedAuthUser.getLogin(), testUser.getUserLogin());

        assertNull(testUser.getFirstName());
        assertNull(testUser.getLastName());
        assertNull(testUser.getEmail());
        assertNull(testUser.getPhone());

        completeDeleteUser(authId);
    }

    @Test
    void update() {
        final AuthUser authUser = new AuthUser("UserUpdate", "Test");
        final Timestamp regTime = getTime();
        final AuthUser savedAuthUser = authUserDao.createAndSave(authUser, regTime);
        final long authId = savedAuthUser.getId();

        final User userToUpdate = new User
                ("First", "Last", "email", "phone", authId);

        final boolean isUpdated = userDAO.update(userToUpdate);
        assertTrue(isUpdated);

        final User afterUpdate = userDAO.getById(authId);
        assertEquals(userToUpdate.getFirstName(), afterUpdate.getFirstName());
        assertEquals(userToUpdate.getLastName(), afterUpdate.getLastName());
        assertEquals(userToUpdate.getEmail(), afterUpdate.getEmail());
        assertEquals(userToUpdate.getPhone(), afterUpdate.getPhone());

        completeDeleteUser(authId);
    }

    private void completeDeleteUser(long id) {
        authUserDao.delete(id);
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("delete from auth_user where id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}