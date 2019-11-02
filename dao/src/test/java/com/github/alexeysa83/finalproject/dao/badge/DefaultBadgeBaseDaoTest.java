package com.github.alexeysa83.finalproject.dao.badge;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.user.DefaultUserInfoBaseDao;
import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import static org.junit.jupiter.api.Assertions.*;

class DefaultBadgeBaseDaoTest {

    private final AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private final UserInfoBaseDao userDAO = DefaultUserInfoBaseDao.getInstance();
    private final BadgeBaseDao badgeDao = DefaultBadgeBaseDao.getInstance();

    private BadgeDto createBadgeDto(String name) {
        BadgeDto badgeDto = new BadgeDto();
        badgeDto.setBadgeName(name);
        return badgeDto;
    }

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    private AuthUserDto createAuthUserDto(String name) {
        UserInfoDto userInfoDto = new UserInfoDto(getTime());
        return new AuthUserDto(name, name + "Pass", userInfoDto);
    }

    @Test
    void getInstance() {
        assertNotNull(badgeDao);
    }

    @Test
    void add() {
        final BadgeDto testBadge = createBadgeDto("CreateTestBadge");
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
        final BadgeDto testBadge = createBadgeDto(testName);
        final BadgeDto savedBadge = badgeDao.add(testBadge);
        assertNotNull(savedBadge);

        final boolean nameIsTaken = badgeDao.isNameTaken(testName);
        assertTrue(nameIsTaken);

        badgeDao.delete(savedBadge.getId());
    }

    @Test
    void isNameTakenFalse() {
        final String testName = "NameFreeTestBadge";
        final boolean nameIsTaken = badgeDao.isNameTaken(testName);
        assertFalse(nameIsTaken);
    }

    @Test
    void getByIdExist() {
        final BadgeDto badge = createBadgeDto("GetByIdTestBadge");
        final BadgeDto testBadge = badgeDao.add(badge);
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
        final BadgeDto badge = createBadgeDto("UpdateTestBadge");
        final BadgeDto testBadge = badgeDao.add(badge);
        final long id = testBadge.getId();
        final BadgeDto badgeToUpdate = createBadgeDto("Updated");
        badgeToUpdate.setId(id);

        final boolean isUpdated = badgeDao.update(badgeToUpdate);
        assertTrue(isUpdated);

        final BadgeDto afterUpdate = badgeDao.getById(id);
        assertEquals(badgeToUpdate.getBadgeName(), afterUpdate.getBadgeName());

        badgeDao.delete(id);
    }

    @Test
    void updateFail() {
        final BadgeDto badgeToUpdate = createBadgeDto("Updated");
        badgeToUpdate.setId(0);

        final boolean isUpdated = badgeDao.update(badgeToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void delete() {
        final AuthUserDto user = createAuthUserDto("DeleteTestBadge");
        final AuthUserDto testUser = authUserDao.add(user);
        final BadgeDto badge = createBadgeDto("DeleteTestBadge");
        final BadgeDto testBadge = badgeDao.add(badge);
        final long badgeId = testBadge.getId();
        userDAO.addBadgeToUser(testUser.getId(), badgeId);

        final BadgeDto badgeToDelete = badgeDao.getById(badgeId);
        assertNotNull(badgeToDelete);

        final boolean isDeleted = badgeDao.delete(badgeId);
        assertTrue(isDeleted);

        final BadgeDto afterDelete = badgeDao.getById(badgeId);
        assertNull(afterDelete);
        
        completeDeleteUser(testUser.getId());
    }

    private void completeDeleteUser(long id) {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        AuthUserEntity toDelete = entityManager.find(AuthUserEntity.class, id);
        entityManager.remove(toDelete);
        entityManager.getTransaction().commit();
        entityManager.clear();
    }
}