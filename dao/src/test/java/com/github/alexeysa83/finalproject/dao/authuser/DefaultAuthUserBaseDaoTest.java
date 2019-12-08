package com.github.alexeysa83.finalproject.dao.authuser;

import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfig.class, AddDeleteTestEntity.class})
@Transactional
class DefaultAuthUserBaseDaoTest {

    @Autowired
    private AuthUserBaseDao authUserDao;
    @Autowired
    private UserInfoBaseDao userInfoDao;
    @Autowired
    private NewsBaseDao newsDao;
    @Autowired
    private AddDeleteTestEntity util;

    /**
     * @Cacheable not needed?
     */

    @Test
    void cacheTest() {
        authUserDao.getById(28L);
        authUserDao.getById(28L);
        authUserDao.getById(28L);
    }

    @Test
    void add() {
        final String testLogin = "CreateTestAuth";
        final AuthUserDto testUser = util.createAuthUserDto(testLogin);
        final AuthUserDto savedUser = authUserDao.add(testUser);
        assertNotNull(savedUser);

        final Long id = savedUser.getId();
        assertNotNull(id);

        assertEquals(testUser.getLogin(), savedUser.getLogin());
        assertEquals(testUser.getPassword(), savedUser.getPassword());
        assertEquals(testUser.getRole(), savedUser.getRole());
        assertEquals(testUser.isDeleted(), savedUser.isDeleted());
    }

    @Test
    void getByLoginExist() {
        final String testLogin = "GetByLoginTestAuth";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testLogin);

        final AuthUserDto userFromDB = authUserDao.getByLogin(testLogin);
        assertNotNull(userFromDB);
        assertEquals(testUser.getId(), userFromDB.getId());
        assertEquals(testLogin, userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isDeleted(), userFromDB.isDeleted());
    }

    @Test
    void getByLoginNotExist() {
        final String fakeLogin = "GetByLoginFakeTestAuth";
        final AuthUserDto userFromDB = authUserDao.getByLogin(fakeLogin);
        assertNull(userFromDB);
    }

    @Test
    void getByIdExist() {
        final String testLogin = "GetByIdTestAuth";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testLogin);
        final Long id = testUser.getId();

        final AuthUserDto userFromDB = authUserDao.getById(id);
        assertNotNull(userFromDB);
        assertEquals(id, userFromDB.getId());
        assertEquals(testUser.getLogin(), userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());
        assertEquals(testUser.getRole(), userFromDB.getRole());
        assertEquals(testUser.isDeleted(), userFromDB.isDeleted());
    }

    @Test
    void getByIdNotExist() {
        final AuthUserDto userFromDB = authUserDao.getById(0L);
        assertNull(userFromDB);
    }

    @Test
    void updateSuccess() {
        final String testLogin = "UpdateTestAuth";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testLogin);
        final Long id = testUser.getId();
        final UserInfoDto testUserInfoDto = util.addTestUserInfoToDB(id);

/**
 * Optimization
 */
        final AuthUserDto userToUpdate = new AuthUserDto
                (id, "Updated", "updated", Role.ADMIN, true, new UserInfoDto());

        final boolean isUpdated = authUserDao.update(userToUpdate);
        assertTrue(isUpdated);

        final AuthUserDto afterUpdate = authUserDao.getById(id);
        assertEquals(userToUpdate.getLogin(), afterUpdate.getLogin());
        assertEquals(userToUpdate.getPassword(), afterUpdate.getPassword());
        assertEquals(userToUpdate.getRole(), afterUpdate.getRole());

        // check isDeleted mark is not updated
        assertEquals(testUser.isDeleted(), afterUpdate.isDeleted());

//         check UserInfoEntity is not updated
        assertEquals(testUserInfoDto, afterUpdate.getUserInfoDto());
    }

    /**
     * Optimization
     */
    @Test
    void updateFail() {
        final AuthUserDto userToUpdate = new AuthUserDto
                (0L, "Updated", "updated", Role.ADMIN, true, null);
        final boolean isUpdated = authUserDao.update(userToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void deleteSuccess() {
        final String testLogin = "DeleteTestAuth";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testLogin);
        final Long id = testUser.getId();
        util.addTestUserInfoToDB(id);
        final NewsDto testNews = util.addTestNewsToDB(testLogin, testUser);

        final AuthUserDto userToDelete = authUserDao.getById(id);
        assertNotNull(userToDelete);

        final boolean isDeleted = authUserDao.delete(id);
        assertTrue(isDeleted);

        final AuthUserDto afterDelete = authUserDao.getById(id);
        assertTrue(afterDelete.isDeleted());

        // Check User Info is not deleted
        final UserInfoDto userInfoAfterDelete = userInfoDao.getById(id);
        assertNotNull(userInfoAfterDelete);
        // Check User News is not deleted
        final NewsDto newsAfterDeleteUser = newsDao.getById(testNews.getId());
        assertNotNull(newsAfterDeleteUser);
    }

    @Test
    void deleteFail() {
        final boolean isDeleted = authUserDao.delete(0L);
        assertFalse(isDeleted);
    }
}