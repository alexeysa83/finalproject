package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfig.class, AddDeleteTestEntity.class})
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
        final Long id = savedBadge.getId();

        assertNotNull(savedBadge);
        assertNotNull(id);
        assertEquals(testBadge.getBadgeName(), savedBadge.getBadgeName());

        badgeDao.delete(id);
    }

    @Test
    void isNameTakenTrue() {
        final String testName = "NameTakenTestBadge";
        final BadgeDto testBadge = util.addTestBadgeToDB(testName);
        assertNotNull(testBadge);

        final boolean nameIsTaken = badgeDao.isNameTaken(testName);
        assertTrue(nameIsTaken);

        badgeDao.delete(testBadge.getId());
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

        badgeDao.delete(id);
    }

    @Test
    void getByIdNotExist() {
        final BadgeDto badgeFromDB = badgeDao.getById(0);
        assertNull(badgeFromDB);
    }

//    @Test
//    void getAllBadges() {
//        List<BadgeDto> allBadges = badgeDao.getAll();
//        for (BadgeDto allBadge : allBadges) {
//            System.out.println(allBadge);
//        }
//    }

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

        badgeDao.delete(id);
    }

    @Test
    void updateFail() {
        final BadgeDto badgeToUpdate = util.createBadgeDto("Updated");
        badgeToUpdate.setId(0);

        final boolean isUpdated = badgeDao.update(badgeToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void delete() {
        final String testName = "DeleteTestBadge";
        final AuthUserDto testUser = util.addTestUserToDB(testName);

        final BadgeDto testBadge = util.addTestBadgeToDB(testName);
        final long badgeId = testBadge.getId();
        userDAO.addBadgeToUser(testUser.getId(), badgeId);

        final BadgeDto badgeToDelete = badgeDao.getById(badgeId);
        assertNotNull(badgeToDelete);

        final boolean isDeleted = badgeDao.delete(badgeId);
        assertTrue(isDeleted);

        final BadgeDto afterDelete = badgeDao.getById(badgeId);
        assertNull(afterDelete);

        util.completeDeleteUser(testUser.getId());
    }
}