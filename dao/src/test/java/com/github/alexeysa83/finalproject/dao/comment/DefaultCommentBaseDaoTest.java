package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.dao.config.HibernateConfig;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateConfig.class, DaoConfig.class, AddDeleteTestEntity.class})
class DefaultCommentBaseDaoTest {

    @Autowired
    private CommentBaseDao commentDao;
    @Autowired
    private AddDeleteTestEntity util;

    @Test
    void add() {
        final String testName = "CreateCommentTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.createCommentDto(testName, testNews);

        final CommentDto savedComment = commentDao.add(testComment);
        assertNotNull(savedComment);

        final Long id = savedComment.getId();
        assertNotNull(id);
        assertEquals(testComment.getContent(), savedComment.getContent());
        assertEquals(testComment.getCreationTime(), savedComment.getCreationTime());
        assertEquals(testComment.getAuthId(), savedComment.getAuthId());
        assertEquals(testComment.getNewsId(), savedComment.getNewsId());
        assertEquals(testComment.getAuthorComment(), savedComment.getAuthorComment());

        util.completeDeleteUser(user.getId());
    }

    @Test
    void getByIdExist() {
        final String testName = "GetByIdCommentTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);

        final long id = testComment.getId();
        final CommentDto commentFromDB = commentDao.getById(id);

        assertNotNull(commentFromDB);
        assertEquals(testComment.getId(), commentFromDB.getId());
        assertEquals(testComment.getContent(), commentFromDB.getContent());
        assertEquals(testComment.getCreationTime(), commentFromDB.getCreationTime());
        assertEquals(testComment.getAuthId(), commentFromDB.getAuthId());
        assertEquals(testComment.getNewsId(), commentFromDB.getNewsId());
        assertEquals(testComment.getAuthorComment(), commentFromDB.getAuthorComment());

        util.completeDeleteUser(user.getId());
    }

    @Test
    void getByIdNotExist() {
        final CommentDto commentFromDB = commentDao.getById(0);
        assertNull(commentFromDB);
    }

    @Test
    void getCommentsOnNewsHaveComments() {
        final String testName = "GetCommentOnNewsTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);
        List<CommentDto> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final CommentDto testComment = util.addTestCommentToDB(testName + i, testNews);
            testList.add(testComment);
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
        }
        util.completeDeleteUser(user.getId());
    }

    @Test
    void getCommentsOnNewsWithoutComments() {
        final String testName = "NoCommentsOnNewsTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        List<CommentDto> listFromDB = commentDao.getCommentsOnNews(testNews.getId());
        assertNotNull(listFromDB);
        util.completeDeleteUser(user.getId());
    }

    @Test
    void updateSuccess() {
        final String testName = "UpdateCommentTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final long id = testComment.getId();

        /*Changing the fields which should not be updated
         */
        testNews.setId(0);
        testNews.setAuthId(0);
        testNews.setAuthorNews("FakeAuthor");

        final CommentDto commentToUpdate = util.createCommentDto("UpdateComplete", testNews);
        commentToUpdate.setId(id);

        final boolean isUpdated = commentDao.update(commentToUpdate);
        assertTrue(isUpdated);

        final CommentDto afterUpdate = commentDao.getById(id);
        assertEquals(commentToUpdate.getContent(), afterUpdate.getContent());

        // Check other fields are not updated
        assertEquals(testComment.getCreationTime(), afterUpdate.getCreationTime());
        assertEquals(testComment.getAuthId(), afterUpdate.getAuthId());
        assertEquals(testComment.getNewsId(), afterUpdate.getNewsId());
        assertEquals(testComment.getAuthorComment(), afterUpdate.getAuthorComment());

        util.completeDeleteUser(user.getId());
    }

    @Test
    void updateFail() {
        final String testName = "UpdateCommentFailTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);

        final CommentDto commentToUpdate = util.createCommentDto("UpdateComplete", testNews);
        final boolean isUpdated = commentDao.update(commentToUpdate);
        assertFalse(isUpdated);

        final CommentDto afterUpdate = commentDao.getById(testComment.getId());
        assertEquals(testComment.getContent(), afterUpdate.getContent());
        util.completeDeleteUser(user.getId());
    }

    @Test
    void delete() {
        final String testName = "DeleteCommentTest";
        final AuthUserDto user = util.addTestUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final long id = testComment.getId();
        CommentDto commentToDelete = commentDao.getById(id);
        assertNotNull(commentToDelete);

        final boolean isDeleted = commentDao.delete(id);
        assertTrue(isDeleted);

        final CommentDto afterDelete = commentDao.getById(id);
        assertNull(afterDelete);
        util.completeDeleteUser(user.getId());
    }
}