package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.NewsRatingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DaoConfig.class, AddDeleteTestEntity.class})
@Transactional
class DefaultNewsBaseDaoTest {

    @Autowired
    private NewsBaseDao newsDao;
    @Autowired
    private CommentBaseDao commentDao;
    @Autowired
    private AddDeleteTestEntity util;

    @Test
    void add() {
        final String testName = "CreateNewsTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.createNewsDto(testName, testUser);

        final NewsDto savedNews = newsDao.add(testNews);
        assertNotNull(savedNews);

        final Long id = savedNews.getId();
        assertNotNull(id);
        assertEquals(testNews.getTitle(), savedNews.getTitle());
        assertEquals(testNews.getContent(), savedNews.getContent());
        assertEquals(testNews.getCreationTime(), savedNews.getCreationTime());
        assertEquals(testNews.getAuthId(), savedNews.getAuthId());
        assertEquals(testNews.getAuthorNews(), savedNews.getAuthorNews());
    }

    @Test
    void getByIdExist() {
        final String testName = "GetByIdNewsTest";
        final Integer testRate = 1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        final Long newsId = testNews.getId();
        util.addTestNewsRatingToDB(testUser.getId(), newsId, testRate);

        final NewsDto newsFromDB = newsDao.getById(newsId);
        assertNotNull(newsFromDB);
        assertEquals(newsId, newsFromDB.getId());
        assertEquals(testNews.getTitle(), newsFromDB.getTitle());
        assertEquals(testNews.getContent(), newsFromDB.getContent());
        assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
        assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
        assertEquals(testNews.getAuthorNews(), newsFromDB.getAuthorNews());

        assertEquals(testRate, newsFromDB.getRatingTotal());
        assertNull(newsFromDB.getUserInSessionRateOnThisNews());
    }

    @Test
    void getByIdNotExist() {
        final NewsDto newsFromDB = newsDao.getById(0L);
        assertNull(newsFromDB);
    }

    @Test
    void getRowsNews() {
        final String testName = "GetCountNewsTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);

        final int testCount = 3;

        for (int i = 0; i < testCount; i++) {
            util.addTestNewsToDB(testName + i, testUser);
        }

        final int countFromDB = newsDao.getRows();
        assertEquals(testCount, countFromDB);
    }

    @Test
    void getNewsOnPage() {
        final String testName = "GetNewsOnPageTest";
        final Integer testRate = 1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final Long authId = testUser.getId();
        final int PAGE_SIZE = 10;
        LinkedList<NewsDto> testList = new LinkedList<>();
        for (int i = 0; i < PAGE_SIZE * 2; i++) {
            final NewsDto testNews = util.addTestNewsToDB(testName + i, testUser);
            util.addTestNewsRatingToDB(authId, testNews.getId(), testRate);
            testList.addFirst(testNews);
        }

        for (int page = 2; page > 0; page--) {
            List<NewsDto> listDB = newsDao.getNewsOnPage(page, PAGE_SIZE);
            assertEquals(PAGE_SIZE, listDB.size());
            int offset = (page - 1) * PAGE_SIZE;
            for (int i = 0; i < PAGE_SIZE; i++) {
                final NewsDto testNews = testList.get(i + offset);
                final NewsDto newsFromDB = listDB.get(i);
                assertNotNull(newsFromDB);
                assertEquals(testNews.getId(), newsFromDB.getId());
                assertEquals(testNews.getTitle(), newsFromDB.getTitle());
                assertEquals(testNews.getContent(), newsFromDB.getContent());
                assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
                assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
                assertEquals(testNews.getAuthorNews(), newsFromDB.getAuthorNews());

                assertEquals(testRate, newsFromDB.getRatingTotal());
                assertNull(newsFromDB.getUserInSessionRateOnThisNews());
            }
        }
    }

    @Test
    void updateSuccess() {
        final String testName = "UpdateNewsTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        final Long id = testNews.getId();

         /*FakeUser is needed to create different from test news authid and author login fields
         which should not be updated
          */
        testUser.setLogin("FakeUser");
        testUser.setId(0L);
        final NewsDto newsToUpdate = util.createNewsDto("UpdateNewsComplete", testUser);
        newsToUpdate.setId(id);

        final boolean isUpdated = newsDao.update(newsToUpdate);
        assertTrue(isUpdated);

        final NewsDto afterUpdate = newsDao.getById(id);
        assertEquals(newsToUpdate.getTitle(), afterUpdate.getTitle());
        assertEquals(newsToUpdate.getContent(), afterUpdate.getContent());

        //Check fields not updated
        assertEquals(testNews.getCreationTime(), afterUpdate.getCreationTime());
        assertEquals(testNews.getAuthId(), afterUpdate.getAuthId());
        assertEquals(testNews.getAuthorNews(), afterUpdate.getAuthorNews());
    }

    @Test
    void updateFail() {
        final AuthUserDto fakeUser = util.createAuthUserDto("FakeUser");
        final NewsDto newsToUpdate = util.createNewsDto("UpdateNewsComplete", fakeUser);
        final boolean isUpdated = newsDao.update(newsToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void deleteSuccess() {
        final String testName = "DeleteNewsTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        final Long newsId = testNews.getId();
        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);

        util.addTestNewsRatingToDB(testUser.getId(), newsId, 1);

        NewsDto newsToDelete = newsDao.getById(newsId);
        assertNotNull(newsToDelete);

        final boolean isDeleted = newsDao.delete(newsId);
        assertTrue(isDeleted);

        final NewsDto afterDelete = newsDao.getById(newsId);
        assertNull(afterDelete);

        final CommentDto commentAfterDelete = commentDao.getById(testComment.getId());
        assertNull(commentAfterDelete);
    }

    @Test
    void deleteFail() {
        final boolean isDeleted = newsDao.delete(0L);
        assertFalse(isDeleted);
    }

    // Also testing getRatingOnNewsFromUser if rating exist
    @Test
    void addRatingOnNews() {
        final String testName = "AddRatingOnNewsTest";
        final Integer rate = 1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final Long authId = testUser.getId();

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        final Long newsId = testNews.getId();

        final NewsRatingDto testRating = util.createNewsRatingDto(authId, newsId, rate);

        final boolean isAdded = newsDao.addRatingOnNews(testRating);
        assertTrue(isAdded);

        final Integer rateFromDB = newsDao.getRatingOnNewsFromUser(authId, newsId);
        assertEquals(rate, rateFromDB);
    }

    // Also testing getRatingOnNewsFromUser if rating exist and not exist (return null)
    @Test
    void deleteRatingFromNews() {
        final String testName = "DeleteRatingOnNewsTest";
        final Integer rate = -1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final Long authId = testUser.getId();

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        final Long newsId = testNews.getId();

        final NewsRatingDto testRating = util.createNewsRatingDto(authId, newsId, rate);

        final boolean isAdded = newsDao.addRatingOnNews(testRating);
        assertTrue(isAdded);

        final Integer rateToDelete = newsDao.getRatingOnNewsFromUser(authId, newsId);
        assertNotNull(rateToDelete);

        final boolean isDeleted = newsDao.deleteRatingFromNews(testRating);
        assertTrue(isDeleted);

        final Integer rateAfterDelete = newsDao.getRatingOnNewsFromUser(authId, newsId);
        assertNull(rateAfterDelete);
    }

    @Test
    void deleteRatingFail() {
        final NewsRatingDto testRating = util.createNewsRatingDto(0L, 0L, 1);
        final boolean isDeleted = newsDao.deleteRatingFromNews(testRating);
        assertFalse(isDeleted);
    }

    @Test
    void getTotalRatingOnNews() {
        final String testName = "GetTotalRatingOnNewsTest";
        final Integer rate = -1;
        final Integer rate2 = 1;
        final Integer rate3 = 1;
        final AuthUserDto authorUser = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, authorUser);
        final Long newsId = testNews.getId();

        final AuthUserDto testUser = util.addTestAuthUserToDB(testName + "1");
        final AuthUserDto testUser2 = util.addTestAuthUserToDB(testName + "2");
        final AuthUserDto testUser3 = util.addTestAuthUserToDB(testName + "3");
        util.addTestNewsRatingToDB(testUser.getId(), newsId, rate);
        util.addTestNewsRatingToDB(testUser2.getId(), newsId, rate2);
        util.addTestNewsRatingToDB(testUser3.getId(), newsId, rate3);

        final int totalRatingFromDB = newsDao.getTotalRatingOnNews(newsId);
        final int expectedTotalRating = rate + rate2 + rate3;
        assertEquals(expectedTotalRating, totalRatingFromDB);
    }

    @Test
    void totalRatingZero() {
        final String testName = "GetTotalRatingZeroOnNewsTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        final int totalRatingFromDB = newsDao.getTotalRatingOnNews(testNews.getId());
        assertEquals(0, totalRatingFromDB);
    }
}