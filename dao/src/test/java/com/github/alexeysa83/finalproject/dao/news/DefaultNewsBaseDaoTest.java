package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNewsBaseDaoTest {

    private final NewsBaseDao newsDao = DefaultNewsBaseDao.getInstance();
    private final static AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private static DataSource mysql = DataSource.getInstance();
    private final static AuthUserDto testUser;

    static {
        testUser = authUserDao.createAndSave(new AuthUserDto("NewsTest", "Pass"), new Timestamp(System.currentTimeMillis()));
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
        final NewsDto testNews = new NewsDto
                ("NewsCreate", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto savedNews = newsDao.createAndSave(testNews);
        assertNotNull(savedNews);

        final Long id = savedNews.getId();
        assertNotNull(id);
        assertEquals(testNews.getTitle(), savedNews.getTitle());
        assertEquals(testNews.getContent(), savedNews.getContent());
        assertEquals(testNews.getCreationTime(), savedNews.getCreationTime());
        assertEquals(testNews.getAuthId(), savedNews.getAuthId());
        assertEquals(testNews.getAuthorNews(), savedNews.getAuthorNews());

        newsDao.delete(id);
    }

    @Test
    void getById() {
        final NewsDto news = new NewsDto
                ("NewsId", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        final NewsDto newsFromDB = newsDao.getById(id);

        assertNotNull(newsFromDB);
        assertEquals(id, newsFromDB.getId());
        assertEquals(testNews.getTitle(), newsFromDB.getTitle());
        assertEquals(testNews.getContent(), newsFromDB.getContent());
        assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
        assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
        assertEquals(testNews.getAuthorNews(), newsFromDB.getAuthorNews());

        newsDao.delete(id);
    }

    /**
     * Variable "i" in cycles equals to parameter "limit" in SQL select in getNewsOnPage method
     */
    @Test
    void getNewsOnPage() {
        LinkedList<NewsDto> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final NewsDto news = new NewsDto("NewsOnPage" + i, "TestContentPage" + i,
                    getTime(), testUser.getId(), testUser.getLogin());
            final NewsDto n = newsDao.createAndSave(news);
            testList.addFirst(n);
        }

        List<NewsDto> listDB = newsDao.getNewsOnPage();
        assertEquals(testList.size(), listDB.size());
        for (int i = 0; i < 10; i++) {
            final NewsDto testNews = testList.get(i);
            final NewsDto newsFromDB = listDB.get(i);
            assertNotNull(newsFromDB);
            assertEquals(testNews.getId(), newsFromDB.getId());
            assertEquals(testNews.getTitle(), newsFromDB.getTitle());
            assertEquals(testNews.getContent(), newsFromDB.getContent());
            assertEquals(testNews.getCreationTime(), newsFromDB.getCreationTime());
            assertEquals(testNews.getAuthId(), newsFromDB.getAuthId());
            assertEquals(testNews.getAuthorNews(), newsFromDB.getAuthorNews());

            newsDao.delete(newsFromDB.getId());
        }
    }

    @Test
    void update() {
        final NewsDto news = new NewsDto
                ("NewsUpdate", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        final NewsDto newsToUpdate = new NewsDto
                (id, "UpdateNewsComplete", "UpdateContentComplete",
                        getTime(), testUser.getId(), testUser.getLogin());

        final boolean isUpdated = newsDao.update(newsToUpdate);
        assertTrue(isUpdated);

        final NewsDto afterUpdate = newsDao.getById(id);
        assertEquals(newsToUpdate.getTitle(), afterUpdate.getTitle());
        assertEquals(newsToUpdate.getContent(), afterUpdate.getContent());

        newsDao.delete(id);
    }

    @Test
    void delete() {
        final NewsDto news = new NewsDto
                ("NewsDelete", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        NewsDto newsToDelete = newsDao.getById(id);
        assertNotNull(newsToDelete);

        final boolean isDeleted = newsDao.delete(id);
        assertTrue(isDeleted);

        final NewsDto afterDelete = newsDao.getById(id);
        assertNull(afterDelete);
    }

    @AfterAll
    static void completeDeleteUser() {
        final long id = testUser.getId();
        authUserDao.delete(id);
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("delete from auth_user where id = ?")) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}