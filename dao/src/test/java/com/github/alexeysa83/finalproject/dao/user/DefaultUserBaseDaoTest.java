package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DefaultUserBaseDaoTest {

    private final UserBaseDao userDAO = DefaultUserBaseDao.getInstance();
    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();

    private static EntityManager entityManager;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    private AuthUserDto createTestAuthUser(String name) {
        UserDto userDto = new UserDto(getTime());
        return new AuthUserDto(name, name + "Pass", userDto);
    }

    @BeforeAll
    static void init() {
        entityManager = HibernateUtil.getEntityManager();
    }

    @Test
    void getById() {
        final AuthUserDto user = createTestAuthUser("GetByIdTestUser");
        final AuthUserDto authUser = authUserDao.createAndSave(user);
        final UserDto testUser = authUser.getUserDto();
        final long authId = testUser.getAuthId();

        final UserDto userFromDB = userDAO.getById(authId);
        assertNotNull(userFromDB);

        assertEquals(testUser.getAuthId(), userFromDB.getAuthId());
        assertEquals(testUser.getRegistrationTime(), userFromDB.getRegistrationTime());
        assertEquals(testUser.getUserLogin(), userFromDB.getUserLogin());

        assertNull(userFromDB.getFirstName());
        assertNull(userFromDB.getLastName());
        assertNull(userFromDB.getEmail());
        assertNull(userFromDB.getPhone());

        completeDeleteUser(authId);
    }

    @Test
    void update() {
        final AuthUserDto user = createTestAuthUser("UpdateTestUser");
        final AuthUserDto authUser = authUserDao.createAndSave(user);
        final UserDto testUser = authUser.getUserDto();
        final long authId = testUser.getAuthId();
        final UserDto userDtoToUpdate = new UserDto
                (authId, "First", "Last", getTime(), "email", "phone", "FakeLogin");

        final boolean isUpdated = userDAO.update(userDtoToUpdate);
        assertTrue(isUpdated);

        final UserDto afterUpdate = userDAO.getById(authId);

        assertEquals(userDtoToUpdate.getFirstName(), afterUpdate.getFirstName());
        assertEquals(userDtoToUpdate.getLastName(), afterUpdate.getLastName());
        assertEquals(userDtoToUpdate.getEmail(), afterUpdate.getEmail());
        assertEquals(userDtoToUpdate.getPhone(), afterUpdate.getPhone());

        assertEquals(authId, afterUpdate.getAuthId());
        assertEquals(testUser.getRegistrationTime(), afterUpdate.getRegistrationTime());
        assertEquals(testUser.getUserLogin(), afterUpdate.getUserLogin());

        completeDeleteUser(authId);
    }

    private void completeDeleteUser(long id) {
        entityManager.getTransaction().begin();
        AuthUserEntity toDelete = entityManager.find(AuthUserEntity.class, id);
        entityManager.remove(toDelete);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @AfterAll
    static void close() {
        HibernateUtil.close();
    }
}