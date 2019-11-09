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
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
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
        UserInfoDto userInfoDto = new UserInfoDto(getTime());
        AuthUserDto authUserDto = new AuthUserDto("NewsTestUser", "Pass", userInfoDto);
        testUser = authUserDao.add(authUserDto);
    }

    @Test
    void getInstance() {
        assertNotNull(newsDao);
    }

    @Test
    void add() {
        final NewsDto testNews = new NewsDto
                ("CreateNewsTest", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto savedNews = newsDao.add(testNews);
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
    void getByIdExist() {
        final NewsDto news = new NewsDto
                ("GetByIdNewsTest", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.add(news);
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

    @Test
    void getByIdNotExist() {
        final NewsDto newsFromDB = newsDao.getById(0);
        assertNull(newsFromDB);
    }

//    @Test
//    void getCountNews() {
//        System.out.println(newsDao.getCountNews());
//    }

    @Test
    void getNewsOnPage() {
        final int PAGE_SIZE = 10;
        LinkedList<NewsDto> testList = new LinkedList<>();
        for (int i = 0; i < PAGE_SIZE * 2; i++) {
            final NewsDto news = new NewsDto("GetNewsOnPageTest" + i, "TestContentPage" + i,
                    getTime(), testUser.getId(), testUser.getLogin());
            final NewsDto n = newsDao.add(news);
            testList.addFirst(n);
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

                newsDao.delete(newsFromDB.getId());
            }
        }
    }

    @Test
    void updateSuccess() {
        final NewsDto news = new NewsDto
                ("UpdateNewsTest", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.add(news);
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
    void updateFail() {
        final NewsDto newsToUpdate = new NewsDto
                (0, "UpdateNewsComplete", "UpdateContentComplete",
                        getTime(), 0, "FakeUser");
        final boolean isUpdated = newsDao.update(newsToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void delete() {
        final NewsDto news = new NewsDto
                ("DeleteNewsTest", "TestContent",
                        getTime(), testUser.getId(), testUser.getLogin());
        final NewsDto testNews = newsDao.add(news);
        commentDao.add
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
    }
}