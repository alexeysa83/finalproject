package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
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

class DefaultCommentBaseDaoTest {

    private final static AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private final static NewsBaseDao newsDao = DefaultNewsBaseDao.getInstance();
    private final CommentBaseDao commentDao = DefaultCommentBaseDao.getInstance();
    private static DataSource mysql = DataSource.getInstance();
    private final static AuthUserDto testUser;
    private final static NewsDto testNews;


    static {
        testUser = authUserDao.createAndSave(new AuthUserDto("CommentTest", "Pass"), new Timestamp(System.currentTimeMillis()));
        testNews = newsDao.createAndSave(new NewsDto
                ("CommentTest", "CommentTest", new Timestamp(System.currentTimeMillis()), testUser.getId(), null));
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
        final CommentDto testComment = new CommentDto
                ("CommentCreate", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
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
                ("CommentId", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
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
                    ("CommentId" + i, getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
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
                ("CommentUpdate", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final CommentDto testComment = commentDao.createAndSave(comment);
        final long id = testComment.getId();
        final CommentDto commentToUpdate = new CommentDto
                (id, "UpdateComplete", comment.getCreationTime(),
                        comment.getAuthId(), comment.getNewsId(), comment.getAuthorComment());

        final boolean isUpdated = commentDao.update(commentToUpdate);
        assertTrue(isUpdated);

        final CommentDto afterUpdate = commentDao.getById(id);
        assertEquals(commentToUpdate.getContent(), afterUpdate.getContent());

        commentDao.delete(id);
    }

    @Test
    void delete() {
        final CommentDto comment = new CommentDto
                ("CommentDelete", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
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
    static void completeDeleteUserAndNews() {
        final long authId = testUser.getId();
        authUserDao.delete(authId);
        final long newsId = testNews.getId();
        newsDao.delete(newsId);

        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("delete from auth_user where id = ?")) {
            statement.setLong(1, authId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}