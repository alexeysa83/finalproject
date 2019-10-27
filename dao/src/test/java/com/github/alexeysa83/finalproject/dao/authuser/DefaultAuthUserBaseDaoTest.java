package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.Role;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAuthUserBaseDaoTest {

    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private DataSource mysql = DataSource.getInstance();

    @Test
    void getInstance() {
        assertNotNull(authUserDao);
    }

    @Test
    void createAndSave() {
        final AuthUserDto testUser = new AuthUserDto("AuthCreate", "Test");
        final AuthUserDto savedUser = authUserDao.createAndSave(testUser, new Timestamp(System.currentTimeMillis()));
        assertNotNull(savedUser);

        final Long id = savedUser.getId();
        assertNotNull(id);
        assertEquals(testUser.getLogin(), savedUser.getLogin());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
        assertEquals(testUser.getRole(), savedUser.getRole());
        assertEquals(testUser.isBlocked(), savedUser.isBlocked());

        completeDeleteUser(id);
    }

    @Test
    void getByLogin() {
        final AuthUserDto testUser = authUserDao.createAndSave
                (new AuthUserDto("AuthLogin", "Test"), new Timestamp(System.currentTimeMillis()));
        final String login = testUser.getLogin();
        final AuthUserDto userFromDB = authUserDao.getByLogin(login);

        assertNotNull(userFromDB);
        assertEquals(testUser.getId(), userFromDB.getId());
        assertEquals(login, userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isBlocked(), userFromDB.isBlocked());

        completeDeleteUser(userFromDB.getId());
    }

    @Test
    void getById() {
        final AuthUserDto testUser = authUserDao.createAndSave
                (new AuthUserDto("AuthId", "Test"), new Timestamp(System.currentTimeMillis()));
        final long id = testUser.getId();
        final AuthUserDto userFromDB = authUserDao.getById(id);

        assertNotNull(userFromDB);
        assertEquals(id, userFromDB.getId());
        assertEquals(testUser.getLogin(), userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isBlocked(), userFromDB.isBlocked());

        completeDeleteUser(id);
    }

    @Test
    void update() {
        final AuthUserDto testUser = authUserDao.createAndSave
                (new AuthUserDto("AuthUpdate", "Test"), new Timestamp(System.currentTimeMillis()));
        final long id = testUser.getId();
        final AuthUserDto userToUpdate = new AuthUserDto(id, "Updated", "updated", Role.ADMIN, false);

        final boolean isUpdated = authUserDao.update(userToUpdate);
        assertTrue(isUpdated);

        final AuthUserDto afterUpdate = authUserDao.getById(id);
        assertEquals(userToUpdate.getLogin(), afterUpdate.getLogin());
        assertEquals(userToUpdate.getPassword(), afterUpdate.getPassword());
        assertEquals(userToUpdate.getRole(), afterUpdate.getRole());

        completeDeleteUser(id);
    }

    @Test
    void delete() {
        final AuthUserDto testUser = authUserDao.createAndSave
                (new AuthUserDto("AuthDelete", "Test"), new Timestamp(System.currentTimeMillis()));
        final long id = testUser.getId();
        final AuthUserDto userToDelete = authUserDao.getById(id);
        assertNotNull(userToDelete);

        final boolean isDeleted = authUserDao.delete(id);
        assertTrue(isDeleted);

        final AuthUserDto afterDelete = authUserDao.getById(id);
        assertTrue(afterDelete.isBlocked());

        completeDeleteUser(id);
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