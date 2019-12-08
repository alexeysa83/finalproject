package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfig.class, AddDeleteTestEntity.class})
@Transactional
class DefaultBadgeBaseDaoTest {

    @Autowired
    private UserInfoBaseDao userDAO;
    @Autowired
    private BadgeBaseDao badgeDao;
    @Autowired
    private AddDeleteTestEntity util;

    @Test
    void add() {
        final String testName = "CreateTestBadge";
        final BadgeDto testBadge = util.createBadgeDto(testName);
        final BadgeDto savedBadge = badgeDao.add(testBadge);

        assertNotNull(savedBadge);
        assertNotNull(savedBadge.getId());
        assertEquals(testBadge.getBadgeName(), savedBadge.getBadgeName());
    }

    @Test
    void isNameTakenTrue() {
        final String testName = "NameTakenTestBadge";
        final BadgeDto testBadge = util.addTestBadgeToDB(testName);
        assertNotNull(testBadge);

        final boolean nameIsTaken = badgeDao.isNameTaken(testName);
        assertTrue(nameIsTaken);
    }

    @Test
    void isNameTakenFalse() {
        final String testName = "NameFreeTestBadge";
        final boolean nameIsTaken = badgeDao.isNameTaken(testName);
        assertFalse(nameIsTaken);
    }

    @Test
    void getByIdExist() {
        final String testName = "GetByIdTestBadge";
        final BadgeDto testBadge = util.addTestBadgeToDB(testName);
        final long id = testBadge.getId();
        final BadgeDto badgeFromDB = badgeDao.getById(id);

        assertNotNull(badgeFromDB);
        assertEquals(id, badgeFromDB.getId());
        assertEquals(testBadge.getBadgeName(), badgeFromDB.getBadgeName());
    }

    @Test
    void getByIdNotExist() {
        final BadgeDto badgeFromDB = badgeDao.getById(0L);
        assertNull(badgeFromDB);
    }

    @Test
    void getAllBadges() {
        final String third = "Third";
        final BadgeDto thirdTestBadge = util.addTestBadgeToDB(third);

        final String first = "First";
        final BadgeDto firstTestBadge = util.addTestBadgeToDB(first);

        final String second = "Second";
        final BadgeDto secondTestBadge = util.addTestBadgeToDB(second);

        List<BadgeDto> allBadges = badgeDao.getAll();
        final BadgeDto firstBadgeFromDB = allBadges.get(0);
        assertEquals(firstTestBadge,firstBadgeFromDB);

        final BadgeDto secondBadgeFromDB = allBadges.get(1);
        assertEquals(secondTestBadge,secondBadgeFromDB);

        final BadgeDto thirdBadgeFromDB = allBadges.get(2);
        assertEquals(thirdTestBadge,thirdBadgeFromDB);
    }

    @Test
    void updateSuccess() {
        final String testName = "UpdateTestBadge";
        final BadgeDto testBadge = util.addTestBadgeToDB(testName);
        final long id = testBadge.getId();
        final BadgeDto badgeToUpdate = util.createBadgeDto("Updated");
        badgeToUpdate.setId(id);

        final boolean isUpdated = badgeDao.update(badgeToUpdate);
        assertTrue(isUpdated);

        final BadgeDto afterUpdate = badgeDao.getById(id);
        assertEquals(badgeToUpdate.getBadgeName(), afterUpdate.getBadgeName());
    }

    @Test
    void updateFail() {
        final BadgeDto badgeToUpdate = util.createBadgeDto("Updated");
        badgeToUpdate.setId(0L);

        final boolean isUpdated = badgeDao.update(badgeToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void deleteSuccess() {
        final String testName = "DeleteTestBadge";
        final AuthUserDto testAuthUser = util.addTestAuthUserToDB(testName);
        final UserInfoDto testUser = util.addTestUserInfoToDB(testAuthUser.getId());

        final BadgeDto testBadge = util.addTestBadgeToDB(testName);
        final Long badgeId = testBadge.getId();

        userDAO.addBadgeToUser(testUser.getAuthId(), badgeId);

        final BadgeDto badgeToDelete = badgeDao.getById(badgeId);
        assertNotNull(badgeToDelete);

        final boolean isDeleted = badgeDao.delete(badgeId);
        assertTrue(isDeleted);

        final BadgeDto afterDelete = badgeDao.getById(badgeId);
        assertNull(afterDelete);
    }

    @Test
    void deleteFail() {
        final boolean isDeleted = badgeDao.delete(0L);
        assertFalse(isDeleted);
    }
}