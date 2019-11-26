package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.badge.BadgeBaseDao;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = {DaoConfig.class, AddDeleteTestEntity.class})
class DefaultUserInfoBaseDaoTest {

    @Autowired
    private UserInfoBaseDao userDAO;
    @Autowired
    private BadgeBaseDao badgeDao;
    @Autowired
    private AddDeleteTestEntity util;

     @Test
    void getByIdExist() {
        final String testLogin = "GetByIdTestUserInfo";
        final AuthUserDto authUser = util.addTestUserToDB(testLogin);
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

        util.completeDeleteUser(authId);
         }

    @Test
    void getByIdNotExist() {
        final UserInfoDto userFromDB = userDAO.getById(0);
        assertNull(userFromDB);
    }

    @Test
    void updateSuccess() {
        final String testLogin = "UpdateTestUserInfo";
        final AuthUserDto authUser = util.addTestUserToDB(testLogin);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();

        /**
         * Optimization
         */
        final UserInfoDto userInfoDtoToUpdate = util.createUserInfoDto();
        userInfoDtoToUpdate.setAuthId(authId);
        userInfoDtoToUpdate.setFirstName("First");
        userInfoDtoToUpdate.setLastName("Last");
        userInfoDtoToUpdate.setEmail("email");
        userInfoDtoToUpdate.setPhone("phone");
        userInfoDtoToUpdate.setUserLogin("FakeLogin");

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

        util.completeDeleteUser(authId);
    }

    @Test
    void updateFail() {
        final UserInfoDto userInfoDtoToUpdate = util.createUserInfoDto();
        userInfoDtoToUpdate.setAuthId(0);
        userInfoDtoToUpdate.setFirstName("First");
        userInfoDtoToUpdate.setLastName("Last");
        userInfoDtoToUpdate.setEmail("email");
        userInfoDtoToUpdate.setPhone("phone");
        userInfoDtoToUpdate.setUserLogin("FakeLogin");

        final boolean isUpdated = userDAO.update(userInfoDtoToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void addBadgeToUser() {
        final String testLogin = "AddBadgeToUserInfoTest";
        final AuthUserDto authUser = util.addTestUserToDB(testLogin);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();

        final BadgeDto testBadge = util.addTestBadgeToDB(testLogin);
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
        util.completeDeleteUser(authId);
    }

    @Test
    void deleteBadgeFromUser() {
        final String testLogin = "DeleteBadgeFromUserInfoTest";
        final AuthUserDto authUser = util.addTestUserToDB(testLogin);
        final UserInfoDto testUser = authUser.getUserInfoDto();
        final long authId = testUser.getAuthId();

        final BadgeDto testBadge = util.addTestBadgeToDB(testLogin);
        final long badgeId = testBadge.getId();
        final UserInfoDto userWithBadge = userDAO.addBadgeToUser(authId, badgeId);
        assertNotNull(userWithBadge);
        assertNotNull(userWithBadge.getBadges());

        final UserInfoDto userDeletedBadge = userDAO.deleteBadgeFromUser(authId, badgeId);
        assertNull(userDeletedBadge.getBadges());

        badgeDao.delete(badgeId);
        util.completeDeleteUser(authId);
    }
}