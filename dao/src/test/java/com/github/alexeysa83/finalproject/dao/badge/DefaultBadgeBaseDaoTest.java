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
import java.util.Set;

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
    void addBadge() {
        final BadgeDto testBadge = createBadgeDto("CreateTestBadge");
        final BadgeDto savedBadge = badgeDao.addBadge(testBadge);
        final Long id = savedBadge.getId();

        assertNotNull(savedBadge);
        assertNotNull(id);
        assertEquals(testBadge.getBadgeName(), savedBadge.getBadgeName());

        badgeDao.deleteBadge(id);
    }

    @Test
    void getById() {
        final BadgeDto badge = createBadgeDto("GetByIdTestBadge");
        final BadgeDto testBadge = badgeDao.addBadge(badge);
        final long id = testBadge.getId();
        final BadgeDto badgeFromDB = badgeDao.getById(id);

        assertNotNull(badgeFromDB);
        assertEquals(id, badgeFromDB.getId());
        assertEquals(testBadge.getBadgeName(), badgeFromDB.getBadgeName());

        badgeDao.deleteBadge(id);
    }

    //????????
    @Test
    void getAllBadges() {
        Set<BadgeDto> allBadges = badgeDao.getAllBadges();
        for (BadgeDto allBadge : allBadges) {
            System.out.println(allBadge);
        }
    }

    @Test
    void update() {
        final BadgeDto badge = createBadgeDto("UpdateTestBadge");
        final BadgeDto testBadge = badgeDao.addBadge(badge);
        final long id = testBadge.getId();
        final BadgeDto badgeToUpdate = createBadgeDto("Updated");
        badgeToUpdate.setId(id);

        final boolean isUpdated = badgeDao.update(badgeToUpdate);
        assertTrue(isUpdated);

        final BadgeDto afterUpdate = badgeDao.getById(id);
        assertEquals(badgeToUpdate.getBadgeName(), afterUpdate.getBadgeName());

        badgeDao.deleteBadge(id);
    }

    @Test
    void deleteBadge() {
        final AuthUserDto user = createAuthUserDto("DeleteTestBadge");
        final AuthUserDto testUser = authUserDao.createAndSave(user);
        final BadgeDto badge = createBadgeDto("DeleteTestBadge");
        final BadgeDto testBadge = badgeDao.addBadge(badge);
        final long badgeId = testBadge.getId();
        userDAO.addBadgeToUser(testUser.getId(), badgeId);

        final BadgeDto badgeToDelete = badgeDao.getById(badgeId);
        assertNotNull(badgeToDelete);

        final boolean isDeleted = badgeDao.deleteBadge(badgeId);
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