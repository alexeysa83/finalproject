package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
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

class DefaultCommentBaseDaoTest {

    private final static AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private final static NewsBaseDao newsDao = DefaultNewsBaseDao.getInstance();
    private final CommentBaseDao commentDao = DefaultCommentBaseDao.getInstance();

    private static AuthUserDto testUser;
    private static NewsDto testNews;

    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static Timestamp getTime() {
        String time = sdf.format(System.currentTimeMillis());
        return Timestamp.valueOf(time);
    }

    @BeforeAll
    static void init() {
        UserDto userDto = new UserDto(getTime());
        AuthUserDto authUserDto = new AuthUserDto("CommentTestUser", "Pass", userDto);
        testUser = authUserDao.createAndSave(authUserDto);
        NewsDto newsDto = new NewsDto("CommentTestNews", "CommentTest", getTime(), testUser.getId(), testUser.getLogin());
        testNews = newsDao.createAndSave(newsDto);
    }

    @Test
    void createAndSave() {
        final CommentDto testComment = new CommentDto
                ("CreateCommentTest", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final CommentDto savedComment = commentDao.createAndSave(testComment);
        assertNotNull(savedComment);

        final Long id = savedComment.getId();
        assertNotNull(id);
        assertEquals(testComment.getContent(), savedComment.getContent());
        assertEquals(testComment.getCreationTime(), savedComment.getCreationTime());
        assertEquals(testComment.getAuthId(), savedComment.getAuthId());
        assertEquals(testComment.getNewsId(), savedComment.getNewsId());
        assertEquals(testComment.getAuthorComment(), savedComment.getAuthorComment());

        commentDao.delete(id);
    }

    @Test
    void getById() {
        final CommentDto comment = new CommentDto
                ("GetByIdCommentTest", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final CommentDto testComment = commentDao.createAndSave(comment);
        final long id = testComment.getId();
        final CommentDto commentFromDB = commentDao.getById(id);

        assertNotNull(commentFromDB);
        assertEquals(testComment.getId(), commentFromDB.getId());
        assertEquals(testComment.getContent(), commentFromDB.getContent());
        assertEquals(testComment.getCreationTime(), commentFromDB.getCreationTime());
        assertEquals(testComment.getAuthId(), commentFromDB.getAuthId());
        assertEquals(testComment.getNewsId(), commentFromDB.getNewsId());
        assertEquals(testComment.getAuthorComment(), commentFromDB.getAuthorComment());

        commentDao.delete(id);
    }

    @Test
    void getCommentsOnNews() {
        List<CommentDto> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final CommentDto comment = new CommentDto
                    ("GetCommentOnNewsTest" + i, getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
            final CommentDto m = commentDao.createAndSave(comment);
            testList.add(m);
        }

        List<CommentDto> listFromDB = commentDao.getCommentsOnNews(testNews.getId());
        assertEquals(testList.size(), listFromDB.size());
        for (int i = 0; i < 10; i++) {
            final CommentDto testComment = testList.get(i);
            final CommentDto commentFromDB = listFromDB.get(i);
            assertNotNull(commentFromDB);
            assertEquals(testComment.getId(), commentFromDB.getId());
            assertEquals(testComment.getContent(), commentFromDB.getContent());
            assertEquals(testComment.getCreationTime(), commentFromDB.getCreationTime());
            assertEquals(testComment.getAuthId(), commentFromDB.getAuthId());
            assertEquals(testComment.getNewsId(), commentFromDB.getNewsId());
            assertEquals(testComment.getAuthorComment(), commentFromDB.getAuthorComment());

            commentDao.delete(commentFromDB.getId());
        }
    }

    @Test
    void update() {
        final CommentDto comment = new CommentDto
                ("UpdateCommentTest", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final CommentDto testComment = commentDao.createAndSave(comment);
        final long id = testComment.getId();
        final CommentDto commentToUpdate = new CommentDto
                (id, "UpdateComplete", getTime(),
                        0, 0, "FakeAuthor");

        final boolean isUpdated = commentDao.update(commentToUpdate);
        assertTrue(isUpdated);

        final CommentDto afterUpdate = commentDao.getById(id);
        assertEquals(commentToUpdate.getContent(), afterUpdate.getContent());

        // Check other fields are not updated
        assertEquals(testComment.getCreationTime(), afterUpdate.getCreationTime());
        assertEquals(testComment.getAuthId(), afterUpdate.getAuthId());
        assertEquals(testComment.getNewsId(), afterUpdate.getNewsId());
        assertEquals(testComment.getAuthorComment(), afterUpdate.getAuthorComment());

        commentDao.delete(id);
    }

    @Test
    void delete() {
        final CommentDto comment = new CommentDto
                ("DeleteCommentTest", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final CommentDto testComment = commentDao.createAndSave(comment);
        final long id = testComment.getId();
        CommentDto commentToDelete = commentDao.getById(id);
        assertNotNull(commentToDelete);

        final boolean isDeleted = commentDao.delete(id);
        assertTrue(isDeleted);

        final CommentDto afterDelete = commentDao.getById(id);
        assertNull(afterDelete);
    }

    @AfterAll
    static void close() {
        EntityManager entityManager = HibernateUtil.getEntityManager();
        entityManager.getTransaction().begin();
        NewsEntity newsEntity = entityManager.find(NewsEntity.class, testNews.getId());
        entityManager.remove(newsEntity);
        AuthUserEntity authUserEntity = entityManager.find(AuthUserEntity.class, testUser.getId());
        entityManager.remove(authUserEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}