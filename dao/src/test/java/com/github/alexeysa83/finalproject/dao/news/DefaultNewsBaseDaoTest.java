package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.DefaultCommentBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.UserDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultNewsBaseDaoTest {

    private final static AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private final NewsBaseDao newsDao = DefaultNewsBaseDao.getInstance();
    private final CommentBaseDao commentDao = DefaultCommentBaseDao.getInstance();

    private static AuthUserDto testUser;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    @BeforeAll
    static void init() {
        UserDto userDto = new UserDto(getTime());
        AuthUserDto authUserDto = new AuthUserDto("NewsTestUser", "Pass", userDto);
        testUser = authUserDao.createAndSave(authUserDto);
    }

    @Test
    void createAndSave() {
        final NewsDto testNews = new NewsDto
                ("NewsCreateTest", "TestContent",
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
                ("GetByIdTestNews", "TestContent",
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
            final NewsDto news = new NewsDto("NewsOnPageTest" + i, "TestContentPage" + i,
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
                ("UpdateNewsTest", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.createAndSave(news);
        final long id = testNews.getId();
        final NewsDto newsToUpdate = new NewsDto
                (id, "UpdateNewsComplete", "UpdateContentComplete",
                        getTime(), 0, "FakeUser");

        final boolean isUpdated = newsDao.update(newsToUpdate);
        assertTrue(isUpdated);

        final NewsDto afterUpdate = newsDao.getById(id);
        assertEquals(newsToUpdate.getTitle(), afterUpdate.getTitle());
        assertEquals(newsToUpdate.getContent(), afterUpdate.getContent());

        //Check fields not updated
        assertEquals(testNews.getCreationTime(), afterUpdate.getCreationTime());
        assertEquals(testNews.getAuthId(), afterUpdate.getAuthId());
        assertEquals(testNews.getAuthorNews(), afterUpdate.getAuthorNews());

        newsDao.delete(id);
    }

        @Test
    void delete() {
        final NewsDto news = new NewsDto
                ("DeleteNewsTest", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.createAndSave(news);
        commentDao.createAndSave
                (new CommentDto("Comment", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin()));
        final long id = testNews.getId();
        NewsDto newsToDelete = newsDao.getById(id);
        assertNotNull(newsToDelete);

        final boolean isDeleted = newsDao.delete(id);
        assertTrue(isDeleted);

        final NewsDto afterDelete = newsDao.getById(id);
        assertNull(afterDelete);
    }

    @AfterAll
    static void close() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        AuthUserEntity authUserEntity = entityManager.find(AuthUserEntity.class, testUser.getId());
        entityManager.remove(authUserEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        HibernateUtil.close();
    }

    // getNewsOnPage is not Deleted
//    @AfterAll
//    static void close() {
//        EntityManager entityManager = HibernateUtil.getEntityManager();
//        entityManager.getTransaction().begin();
//        AuthUserEntity authUserEntity = entityManager.find(AuthUserEntity.class, testUser.getId());
//        authUserEntity.setNews(null);
//        entityManager.remove(authUserEntity);
//        entityManager.getTransaction().commit();
//        entityManager.close();
//        HibernateUtil.close();
//    }
}