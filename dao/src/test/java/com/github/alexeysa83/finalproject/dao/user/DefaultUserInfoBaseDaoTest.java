package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration (classes = {DaoConfig.class, AddDeleteTestEntity.class})
@Transactional
class DefaultUserInfoBaseDaoTest {

    @Autowired
    private UserInfoBaseDao userDAO;
    @Autowired
    private AddDeleteTestEntity util;

    @Test
    void add() {
        final String testLogin = "CreateTestUserInfo";
        final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testLogin);

        final UserInfoDto testUserInfoDto = util.createUserInfoDto();
        testUserInfoDto.setAuthId(testAuthUser.getId());

        final UserInfoDto savedUser = userDAO.add(testUserInfoDto);
        assertNotNull(savedUser);
        assertEquals(testUserInfoDto.getAuthId(), savedUser.getAuthId());
        assertEquals(testUserInfoDto.getRegistrationTime(), savedUser.getRegistrationTime());
        assertEquals(testAuthUser.getLogin(), savedUser.getUserLogin());

        assertNull(savedUser.getFirstName());
        assertNull(savedUser.getLastName());
        assertNull(savedUser.getEmail());
        assertNull(savedUser.getPhone());
        }


     @Test
    void getByIdExist() {
        final String testLogin = "GetByIdTestUserInfo";
         final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testLogin);
        final UserInfoDto testUser = util.addTestUserInfoToDB(testAuthUser.getId());
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
         }

    @Test
    void getByIdNotExist() {
        final UserInfoDto userFromDB = userDAO.getById(0L);
        assertNull(userFromDB);
    }

    @Test
    void updateSuccess() {
        final String testLogin = "UpdateTestUserInfo";
        final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testLogin);
        final UserInfoDto testUser = util.addTestUserInfoToDB(testAuthUser.getId());
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

        // non-changeable fields
        assertEquals(authId, afterUpdate.getAuthId());
        assertEquals(testUser.getRegistrationTime(), afterUpdate.getRegistrationTime());
        assertEquals(testUser.getUserLogin(), afterUpdate.getUserLogin());
    }

    @Test
    void updateFail() {
        final UserInfoDto userInfoDtoToUpdate = util.createUserInfoDto();
        userInfoDtoToUpdate.setAuthId(0L);

        final boolean isUpdated = userDAO.update(userInfoDtoToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void deleteSuccess() {
        final String testLogin = "DeleteTestUserInfo";
        final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testLogin);
        final UserInfoDto testUser = util.addTestUserInfoToDB(testAuthUser.getId());
        final long authId = testUser.getAuthId();

        final BadgeDto testBadge = util.addTestBadgeToDB(testLogin);
        final Long badgeId = testBadge.getId();
       userDAO.addBadgeToUser(authId, badgeId);

        final UserInfoDto userToDelete = userDAO.getById(authId);
        assertNotNull(userToDelete);

        final boolean isDeleted = userDAO.delete(authId);
        assertTrue(isDeleted);

        final UserInfoDto afterDelete = userDAO.getById(authId);
        assertNull(afterDelete);
            }

    @Test
    void deleteFail() {
        final boolean isDeleted = userDAO.delete(0L);
        assertFalse(isDeleted);
    }


    @Test
    void addBadgeToUserSuccess() {
        final String testLogin = "AddBadgeToUserInfoTest";
        final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testLogin);
        final UserInfoDto testUser = util.addTestUserInfoToDB(testAuthUser.getId());
        final Long authId = testUser.getAuthId();

        final BadgeDto testBadge = util.addTestBadgeToDB(testLogin);
        final Long badgeId = testBadge.getId();

        final UserInfoDto userWithBadge = userDAO.addBadgeToUser(authId, badgeId);
        assertNotNull(userWithBadge);
        assertEquals(testUser, userWithBadge);
        assertNotNull(userWithBadge.getBadges());

        final Iterator<BadgeDto> iterator = userWithBadge.getBadges().iterator();
        assertTrue(iterator.hasNext());

        final BadgeDto addedBadge = iterator.next();
        assertEquals(testBadge.getId(), addedBadge.getId());
        assertEquals(testBadge.getBadgeName(), addedBadge.getBadgeName());
    }

    @Test
    void deleteBadgeFromUser() {
        final String testLogin = "DeleteBadgeFromUserInfoTest";
        final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testLogin);
        final UserInfoDto testUser = util.addTestUserInfoToDB(testAuthUser.getId());
        final Long authId = testUser.getAuthId();

        final BadgeDto testBadge = util.addTestBadgeToDB(testLogin);
        final Long badgeId = testBadge.getId();

        final UserInfoDto userWithBadge = userDAO.addBadgeToUser(authId, badgeId);
        assertNotNull(userWithBadge);
        assertNotNull(userWithBadge.getBadges());

        final UserInfoDto userDeletedBadge = userDAO.deleteBadgeFromUser(authId, badgeId);
        assertEquals(userWithBadge, userDeletedBadge);
        assertNull(userDeletedBadge.getBadges());
    }
}