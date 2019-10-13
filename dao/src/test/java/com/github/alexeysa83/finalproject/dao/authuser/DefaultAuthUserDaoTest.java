package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.Role;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAuthUserDaoTest {

    private final AuthUserDao authUserDao = DefaultAuthUserDao.getInstance();

    @Test
    void getInstance() {
        assertNotNull(authUserDao);
    }

    @Test
    void createAndSave() {
        final AuthUser testUser = new AuthUser("AuthCreate", "Test");
        final AuthUser savedUser = authUserDao.createAndSave(testUser, new Timestamp(System.currentTimeMillis()));
        assertNotNull(savedUser);

        final Long id = savedUser.getId();
        assertNotNull(id);
        assertEquals(testUser.getLogin(), savedUser.getLogin());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
        assertEquals(testUser.getRole(), savedUser.getRole());
        assertEquals(testUser.isBlocked(), savedUser.isBlocked());
    }

    @Test
    void getByLogin() {
        final AuthUser testUser = authUserDao.createAndSave
                (new AuthUser("AuthLogin", "Test"), new Timestamp(System.currentTimeMillis()));
        final String login = testUser.getLogin();
        final AuthUser userFromDB = authUserDao.getByLogin(login);

        assertNotNull(userFromDB);
        assertEquals(testUser.getId(), userFromDB.getId());
        assertEquals(login, userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isBlocked(), userFromDB.isBlocked());
    }

    @Test
    void getById() {
        final AuthUser testUser = authUserDao.createAndSave
                (new AuthUser("AuthId", "Test"), new Timestamp(System.currentTimeMillis()));
        final long id = testUser.getId();
        final AuthUser userFromDB = authUserDao.getById(id);

        assertNotNull(userFromDB);
        assertEquals(id, userFromDB.getId());
        assertEquals(testUser.getLogin(), userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isBlocked(), userFromDB.isBlocked());
    }

    @Test
    void update() {
        final AuthUser testUser = authUserDao.createAndSave
                (new AuthUser("AuthUpdate", "Test"), new Timestamp(System.currentTimeMillis()));
        final long id = testUser.getId();
        final AuthUser userToUpdate = new AuthUser(id, "Updated", "updated", Role.ADMIN, false);

        final boolean isUpdated = authUserDao.update(userToUpdate);
        assertTrue(isUpdated);

        final AuthUser afterUpdate = authUserDao.getById(id);
        assertEquals(userToUpdate.getLogin(), afterUpdate.getLogin());
        assertEquals(userToUpdate.getPassword(), afterUpdate.getPassword());
        assertEquals(userToUpdate.getRole(), afterUpdate.getRole());
    }

    @Test
    void delete() {
        final AuthUser testUser = authUserDao.createAndSave
                (new AuthUser("AuthDelete", "Test"), new Timestamp(System.currentTimeMillis()));
        final long id = testUser.getId();
        final AuthUser userToDelete = authUserDao.getById(id);
        assertNotNull(userToDelete);

        final boolean isDeleted = authUserDao.delete(id);
        assertTrue(isDeleted);

        final AuthUser afterDelete = authUserDao.getById(id);
        assertTrue(afterDelete.isBlocked());
    }
}