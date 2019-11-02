package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.badge.DefaultBadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class DefaultUserInfoBaseDaoTest {

    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private final UserInfoBaseDao userDAO = DefaultUserInfoBaseDao.getInstance();
    private final BadgeBaseDao badgeDao = DefaultBadgeBaseDao.getInstance();

    private static EntityManager entityManager;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    private AuthUserDto createAuthUserDto(String name) {
        UserInfoDto userInfoDto = new UserInfoDto(getTime());
        return new AuthUserDto(name, name + "Pass", userInfoDto);
    }

    private BadgeDto createBadgeDto(String name) {
        BadgeDto badgeDto = new BadgeDto();
        badgeDto.setBadgeName(name);
        return badgeDto;
    }

    @BeforeAll
    static void init() {
        entityManager = HibernateUtil.getEntityManager();
    }

    @Test
    void getInstance() {
        assertNotNull(userDAO);
    }

    @Test
    void getByIdExist() {
        final AuthUserDto user = createAuthUserDto("GetByIdTestUser");
        final AuthUserDto authUser = authUserDao.add(user);
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
    void getByIdNotExist() {
        final UserInfoDto userFromDB = userDAO.getById(0);
        assertNull(userFromDB);
    }

    @Test
    void updateSuccess() {
        final AuthUserDto user = createAuthUserDto("UpdateTestUser");
        final AuthUserDto authUser = authUserDao.add(user);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();
        final UserInfoDto userInfoDtoToUpdate = new UserInfoDto
                (authId, "First", "Last", getTime(),
                        "email", "phone", "FakeLogin");

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

    @Test
    void updateFail() {
        final UserInfoDto userInfoDtoToUpdate = new UserInfoDto
                (0, "First", "Last", getTime(),
                        "email", "phone", "FakeLogin");
        final boolean isUpdated = userDAO.update(userInfoDtoToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void addBadgeToUser() {
        final AuthUserDto user = createAuthUserDto("AddBadgeToUserTest");
        final AuthUserDto authUser = authUserDao.add(user);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();
        final BadgeDto testBadge = badgeDao.add(createBadgeDto("UserTestBadge"));
        final long badgeId = testBadge.getId();

        final UserInfoDto userWithBadge = userDAO.addBadgeToUser(authId, badgeId);
        assertNotNull(userWithBadge);
        assertEquals(testUser, userWithBadge);

        final Iterator<BadgeDto> iterator = userWithBadge.getBadges().iterator();
        assertTrue(iterator.hasNext());

        final BadgeDto addedBadge = iterator.next();
        assertEquals(testBadge.getId(), addedBadge.getId());
        assertEquals(testBadge.getBadgeName(), addedBadge.getBadgeName());

        userDAO.deleteBadgeFromUser(authId, badgeId);
        badgeDao.delete(badgeId);
        completeDeleteUser(authId);
    }

    @Test
    void deleteBadgeFromUser() {
        final AuthUserDto user = createAuthUserDto("DeleteBadgeFromUserTest");
        final AuthUserDto authUser = authUserDao.add(user);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();
        final BadgeDto testBadge = badgeDao.add(createBadgeDto("testBadge"));
        final long badgeId = testBadge.getId();
        final UserInfoDto userWithBadge = userDAO.addBadgeToUser(authId, badgeId);

        assertNotNull(userWithBadge);
        assertNotNull(userWithBadge.getBadges());

        final UserInfoDto userDeletedBadge = userDAO.deleteBadgeFromUser(authId, badgeId);
        assertNull(userDeletedBadge.getBadges());

        badgeDao.delete(badgeId);
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