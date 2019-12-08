package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
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
class DefaultCommentBaseDaoTest {

    @Autowired
    private CommentBaseDao commentDao;
    @Autowired
    private AddDeleteTestEntity util;

    @Test
    void add() {
        final String testName = "CreateCommentTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
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
    }

    @Test
    void getByIdExist() {
        final String testName = "GetByIdCommentTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);

        final Long id = testComment.getId();
        final CommentDto commentFromDB = commentDao.getById(id);

        assertNotNull(commentFromDB);
        assertEquals(testComment.getId(), commentFromDB.getId());
        assertEquals(testComment.getContent(), commentFromDB.getContent());
        assertEquals(testComment.getCreationTime(), commentFromDB.getCreationTime());
        assertEquals(testComment.getAuthId(), commentFromDB.getAuthId());
        assertEquals(testComment.getNewsId(), commentFromDB.getNewsId());
        assertEquals(testComment.getAuthorComment(), commentFromDB.getAuthorComment());
    }

    @Test
    void getByIdNotExist() {
        final CommentDto commentFromDB = commentDao.getById(0L);
        assertNull(commentFromDB);
    }

    @Test
    void getCommentsOnNewsHaveComments() {
        final String testName = "GetCommentOnNewsTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
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
    }

    @Test
    void getCommentsOnNewsWithoutComments() {
        final String testName = "NoCommentsOnNewsTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        List<CommentDto> listFromDB = commentDao.getCommentsOnNews(testNews.getId());
        assertEquals(0, listFromDB.size());
    }

    @Test
    void updateSuccess() {
        final String testName = "UpdateCommentTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final long id = testComment.getId();

        /*Changing the fields which should not be updated
         */
        testNews.setId(0L);
        testNews.setAuthId(0L);
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
    }

    @Test
    void updateFail() {
        final String testName = "UpdateCommentFailTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto commentToUpdate = util.createCommentDto("UpdateComplete", testNews);
        final boolean isUpdated = commentDao.update(commentToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void deleteSuccess() {
        final String testName = "DeleteCommentTest";
        final AuthUserDto user = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, user);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long id = testComment.getId();
        CommentDto commentToDelete = commentDao.getById(id);
        assertNotNull(commentToDelete);

        final boolean isDeleted = commentDao.delete(id);
        assertTrue(isDeleted);

        final CommentDto afterDelete = commentDao.getById(id);
        assertNull(afterDelete);
            }

    @Test
    void deleteFail() {
        final boolean isDeleted = commentDao.delete(0L);
        assertFalse(isDeleted);
    }
}