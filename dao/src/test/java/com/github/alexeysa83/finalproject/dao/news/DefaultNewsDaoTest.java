package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import com.github.alexeysa83.finalproject.model.News;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNewsDaoTest {

    private final NewsDao newsDao = DefaultNewsDao.getInstance();
    private final static AuthUserDao authUserDao = DefaultAuthUserDao.getInstance();
    private final static AuthUser testUser;

    static {
        testUser = authUserDao.createAndSave(new AuthUser("NewsTest", "Pass"), new Timestamp(System.currentTimeMillis()));
    }

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    @Test
    void getInstance() {
        assertNotNull(newsDao);
    }

    @Test
    void createAndSave() {
        final News testNews = new News
                ("NewsCreate", "TestContent",
                        new Timestamp(System.currentTimeMillis()), testUser.getId(), testUser.getLogin());
        final News savedNews = newsDao.createAndSave(testNews);
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
    void getById() {
        final News news = new News
                ("NewsId", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final News testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        final News newsFromDB = newsDao.getById(id);

        assertNotNull(newsFromDB);
        assertEquals(id, newsFromDB.getId());
        assertEquals(testNews.getTitle(), newsFromDB.getTitle());
        assertEquals(testNews.getContent(), newsFromDB.getContent());
        assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
        assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
        assertEquals(testNews.getAuthorNews(), testNews.getAuthorNews());
    }

    /**
     * Variable "i" in cycles equals to parameter "limit" in SQL select in getNewsOnPage method
     */
    @Test
    void getNewsOnPage() {
        LinkedList<News> testList = new LinkedList<>();
                for (int i = 0; i < 10; i++) {
            final News news = new News("NewsOnPage" + i, "TestContentPage" + i,
                    getTime(), testUser.getId(), testUser.getLogin());
            final News n = newsDao.createAndSave(news);
            testList.addFirst(n);
        }

        List<News> listDB = newsDao.getNewsOnPage();
        assertEquals(testList.size(), listDB.size());
        for (int i = 0; i < 10; i++) {
            final News testNews = testList.get(i);
            final News newsFromDB = listDB.get(i);
            assertNotNull(newsFromDB);
            assertEquals(testNews.getId(), newsFromDB.getId());
            assertEquals(testNews.getTitle(), newsFromDB.getTitle());
            assertEquals(testNews.getContent(), newsFromDB.getContent());
            assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
            assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
            assertEquals(testNews.getAuthorNews(), newsFromDB.getAuthorNews());
        }
    }

    @Test
    void update() {
        final News news = new News
                ("NewsUpdate", "TestContent",
                        new Timestamp(System.currentTimeMillis()), testUser.getId(), testUser.getLogin());
        final News testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        final News newsToUpdate = new News
                (id, "UpdateNewsComplete", "UpdateContentComplete");

        final boolean isUpdated = newsDao.update(newsToUpdate);
        assertTrue(isUpdated);

        final News afterUpdate = newsDao.getById(id);
        assertEquals(newsToUpdate.getTitle(), afterUpdate.getTitle());
        assertEquals(newsToUpdate.getContent(), afterUpdate.getContent());
    }

    @Test
    void delete() {
        final News news = new News
                ("NewsDelete", "TestContent",
                        new Timestamp(System.currentTimeMillis()), testUser.getId(), testUser.getLogin());
        final News testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        News newsToDelete = newsDao.getById(id);
        assertNotNull(newsToDelete);

        final boolean isDeleted = newsDao.delete(id);
        assertTrue(isDeleted);

        final News afterDelete = newsDao.getById(id);
        assertNull(afterDelete);
    }
}