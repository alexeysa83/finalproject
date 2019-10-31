package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DefaultAuthUserBaseDaoTest {

    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();

    private static EntityManager entityManager;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Timestamp getTime() {
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
    void createAndSave() {
        final AuthUserDto testUser = createTestAuthUser("CreateTestAuth");
        final AuthUserDto savedUser = authUserDao.createAndSave(testUser);
        assertNotNull(savedUser);

        final Long id = savedUser.getId();
        assertNotNull(id);

        assertEquals(testUser.getLogin(), savedUser.getLogin());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
        assertEquals(testUser.getRole(), savedUser.getRole());
        assertEquals(testUser.isBlocked(), savedUser.isBlocked());

        assertEquals(id, savedUser.getUserInfoDto().getAuthId());
        assertEquals(testUser.getUserInfoDto().getRegistrationTime(), savedUser.getUserInfoDto().getRegistrationTime());

        completeDeleteUser(id);
    }

    @Test
    void getByLogin() {
        final AuthUserDto user = createTestAuthUser("GetByLoginTestAuth");
        final AuthUserDto testUser = authUserDao.createAndSave(user);
        final String login = testUser.getLogin();
        final AuthUserDto userFromDB = authUserDao.getByLogin(login);

        assertNotNull(userFromDB);
        assertEquals(testUser.getId(), userFromDB.getId());
        assertEquals(login, userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isBlocked(), userFromDB.isBlocked());
        assertEquals(testUser.getUserInfoDto(), userFromDB.getUserInfoDto());

        completeDeleteUser(userFromDB.getId());
    }

    @Test
    void getById() {
        final AuthUserDto user = createTestAuthUser("GetByIdTestAuth");
        final AuthUserDto testUser = authUserDao.createAndSave(user);
        final long id = testUser.getId();
        final AuthUserDto userFromDB = authUserDao.getById(id);

        assertNotNull(userFromDB);
        assertEquals(id, userFromDB.getId());
        assertEquals(testUser.getLogin(), userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isBlocked(), userFromDB.isBlocked());
        assertEquals(testUser.getUserInfoDto(), userFromDB.getUserInfoDto());

        completeDeleteUser(id);
    }

    @Test
    void updateSuccess() {
        final AuthUserDto user = createTestAuthUser("UpdateTestAuth");
        final AuthUserDto testUser = authUserDao.createAndSave(user);
        final long id = testUser.getId();
        // Create updated UserDto entity which has not to be updated in AuthUserDao update method
        UserInfoDto userInfoDtoToUpdate = new UserInfoDto(getTime());
        userInfoDtoToUpdate.setFirstName("Update");
        userInfoDtoToUpdate.setAuthId(testUser.getId());

        final AuthUserDto userToUpdate = new AuthUserDto
                (id, "Updated", "updated", Role.ADMIN, true, userInfoDtoToUpdate);

        final boolean isUpdated = authUserDao.update(userToUpdate);
        assertTrue(isUpdated);

        final AuthUserDto afterUpdate = authUserDao.getById(id);
        assertEquals(userToUpdate.getLogin(), afterUpdate.getLogin());
        assertEquals(userToUpdate.getPassword(), afterUpdate.getPassword());
        assertEquals(userToUpdate.getRole(), afterUpdate.getRole());
        assertEquals(userToUpdate.isBlocked(), afterUpdate.isBlocked());
//         check UserEntity is not updated
        assertEquals(testUser.getUserInfoDto(), afterUpdate.getUserInfoDto());
        completeDeleteUser(id);
    }

    @Test
    void updateFail() {
        final AuthUserDto user = createTestAuthUser("UpdateTestAuthFail");
        final AuthUserDto testUser = authUserDao.createAndSave(user);
        long id = testUser.getId();
        final AuthUserDto userToUpdate = new AuthUserDto
                (id, null, "updated", Role.ADMIN, true, null);

        final boolean isUpdated = authUserDao.update(userToUpdate);
        assertFalse(isUpdated);

        completeDeleteUser(id);
    }

    @Test
    void delete() {
        final AuthUserDto user = createTestAuthUser("DeleteTestAuth");
        final AuthUserDto testUser = authUserDao.createAndSave(user);
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