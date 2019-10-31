package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DefaultUserInfoBaseDaoTest {

    private final UserInfoBaseDao userDAO = DefaultUserInfoBaseDao.getInstance();
    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();

    private static EntityManager entityManager;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    private AuthUserDto createTestAuthUser(String name) {
        UserInfoDto userInfoDto = new UserInfoDto(getTime());
        return new AuthUserDto(name, name + "Pass", userInfoDto);
    }

    @BeforeAll
    static void init() {
        entityManager = HibernateUtil.getEntityManager();
    }

    @Test
    void getById() {
        final AuthUserDto user = createTestAuthUser("GetByIdTestUser");
        final AuthUserDto authUser = authUserDao.createAndSave(user);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();

        final UserInfoDto userFromDB = userDAO.getById(authId);
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
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();
        final UserInfoDto userInfoDtoToUpdate = new UserInfoDto
                (authId, "First", "Last", getTime(), "email", "phone", "FakeLogin");

        final boolean isUpdated = userDAO.update(userInfoDtoToUpdate);
        assertTrue(isUpdated);

        final UserInfoDto afterUpdate = userDAO.getById(authId);

        assertEquals(userInfoDtoToUpdate.getFirstName(), afterUpdate.getFirstName());
        assertEquals(userInfoDtoToUpdate.getLastName(), afterUpdate.getLastName());
        assertEquals(userInfoDtoToUpdate.getEmail(), afterUpdate.getEmail());
        assertEquals(userInfoDtoToUpdate.getPhone(), afterUpdate.getPhone());

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
        entityManager.clear();
    }

    @AfterAll
    static void close() {
        entityManager.close();
    }
}