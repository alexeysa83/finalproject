package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
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
        final AuthUserDto user = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.createNewsDto(testName, user);

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
        final AuthUserDto user = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, user);
        final Long id = testNews.getId();
        final NewsDto newsFromDB = newsDao.getById(id);

        assertNotNull(newsFromDB);
        assertEquals(id, newsFromDB.getId());
        assertEquals(testNews.getTitle(), newsFromDB.getTitle());
        assertEquals(testNews.getContent(), newsFromDB.getContent());
        assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
        assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
        assertEquals(testNews.getAuthorNews(), newsFromDB.getAuthorNews());
    }

    @Test
    void getByIdNotExist() {
        final NewsDto newsFromDB = newsDao.getById(0L);
        assertNull(newsFromDB);
    }

    @Test
    void getRowsNews() {
        final String testName = "GetCountNewsTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);

        final int testCount = 3;

        for (int i = 0; i < testCount; i++) {
            util.addTestNewsToDB(testName + i, user);
        }

        final int countFromDB = newsDao.getRows();
        assertEquals(testCount, countFromDB);
    }

    @Test
        void getNewsOnPage() {
        final String testName = "GetNewsOnPageTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
        final int PAGE_SIZE = 10;
        LinkedList<NewsDto> testList = new LinkedList<>();
        for (int i = 0; i < PAGE_SIZE * 2; i++) {
            final NewsDto testNews = util.addTestNewsToDB(testName + i, user);
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
            }
        }
    }

    @Test
    void updateSuccess() {
        final String testName = "UpdateNewsTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, user);
        final Long id = testNews.getId();

         /*FakeUser is needed to create different from test news authid and author login fields
         which should not be updated
          */
        user.setLogin("FakeUser");
        final NewsDto newsToUpdate = util.createNewsDto("UpdateNewsComplete", user);
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
        final AuthUserDto user = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, user);
        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long id = testNews.getId();
        NewsDto newsToDelete = newsDao.getById(id);
        assertNotNull(newsToDelete);

        final boolean isDeleted = newsDao.delete(id);
        assertTrue(isDeleted);

        final NewsDto afterDelete = newsDao.getById(id);
        assertNull(afterDelete);

        final CommentDto commentAfterDelete = commentDao.getById(testComment.getId());
        assertNull(commentAfterDelete);
    }

    @Test
    void deleteFail() {
        final boolean isDeleted = newsDao.delete(0L);
        assertFalse(isDeleted);
    }
}