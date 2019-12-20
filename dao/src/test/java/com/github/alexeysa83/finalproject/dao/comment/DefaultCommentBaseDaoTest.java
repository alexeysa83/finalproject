package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.config.DaoConfig;
import com.github.alexeysa83.finalproject.dao.util.AddDeleteTestEntity;
import com.github.alexeysa83.finalproject.model.dto.*;
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
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

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
        final Integer testRate = 1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long commentId = testComment.getId();
        util.addTestCommentRatingToDB(testUser.getId(), commentId, testRate);

        final CommentDto commentFromDB = commentDao.getById(commentId);
        assertNotNull(commentFromDB);
        assertEquals(testComment.getId(), commentFromDB.getId());
        assertEquals(testComment.getContent(), commentFromDB.getContent());
        assertEquals(testComment.getCreationTime(), commentFromDB.getCreationTime());
        assertEquals(testComment.getAuthId(), commentFromDB.getAuthId());
        assertEquals(testComment.getNewsId(), commentFromDB.getNewsId());
        assertEquals(testComment.getAuthorComment(), commentFromDB.getAuthorComment());

        assertEquals(testRate, commentFromDB.getRatingTotal());
        assertNull(commentFromDB.getUserInSessionRateOnThisComment());
    }

    @Test
    void getByIdNotExist() {
        final CommentDto commentFromDB = commentDao.getById(0L);
        assertNull(commentFromDB);
    }

    @Test
    void getCommentsOnNewsHaveComments() {
        final String testName = "GetCommentOnNewsTest";
        final Integer testRate = 1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final Long authId = testUser.getId();
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        List<CommentDto> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final CommentDto testComment = util.addTestCommentToDB(testName + i, testNews);
            util.addTestCommentRatingToDB(authId, testComment.getId(), testRate);
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

            assertEquals(testRate, commentFromDB.getRatingTotal());
            assertNull(commentFromDB.getUserInSessionRateOnThisComment());
        }
    }

    @Test
    void getCommentsOnNewsWithoutComments() {
        final String testName = "NoCommentsOnNewsTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        List<CommentDto> listFromDB = commentDao.getCommentsOnNews(testNews.getId());
        assertEquals(0, listFromDB.size());
    }

    @Test
    void updateSuccess() {
        final String testName = "UpdateCommentTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

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
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        final CommentDto commentToUpdate = util.createCommentDto("UpdateComplete", testNews);
        final boolean isUpdated = commentDao.update(commentToUpdate);
        assertFalse(isUpdated);
    }

    @Test
    void deleteSuccess() {
        final String testName = "DeleteCommentTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long commentId = testComment.getId();

        util.addTestCommentRatingToDB(testUser.getId(), commentId, 1);

        CommentDto commentToDelete = commentDao.getById(commentId);
        assertNotNull(commentToDelete);

        final boolean isDeleted = commentDao.delete(commentId);
        assertTrue(isDeleted);

        final CommentDto afterDelete = commentDao.getById(commentId);
        assertNull(afterDelete);
    }

    @Test
    void deleteFail() {
        final boolean isDeleted = commentDao.delete(0L);
        assertFalse(isDeleted);
    }

    // Also testing getRatingOnCommentFromUser if rating exist
    @Test
    void addRatingOnComment() {
        final String testName = "AddRatingOnCommentTest";
        final Integer rate = 1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final Long authId = testUser.getId();

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long commentId = testComment.getId();

        final CommentRatingDto testRating = util.createCommentRatingDto(authId, commentId, rate);

        final boolean isAdded = commentDao.addRatingOnComment(testRating);
        assertTrue(isAdded);

        final Integer rateFromDB = commentDao.getRatingOnCommentFromUser(authId, commentId);
        assertEquals(rate, rateFromDB);
    }

    // Also testing getTotalRatingOnComment if rating exist and not exist (return null)
    @Test
    void deleteRatingFromComment() {
        final String testName = "DeleteRatingOnCommentTest";
        final Integer rate = -1;
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final Long authId = testUser.getId();

        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long commentId = testComment.getId();

        final CommentRatingDto testRating = util.createCommentRatingDto(authId, commentId, rate);
        final boolean isAdded = commentDao.addRatingOnComment(testRating);
        assertTrue(isAdded);

        final Integer rateToDelete = commentDao.getRatingOnCommentFromUser(authId, commentId);
        assertNotNull(rateToDelete);

        final boolean isDeleted = commentDao.deleteRatingFromComment(testRating);
        assertTrue(isDeleted);

        final Integer rateAfterDelete = commentDao.getRatingOnCommentFromUser(authId, commentId);
        assertNull(rateAfterDelete);
    }

    @Test
    void deleteRatingFail() {
        final CommentRatingDto testRating = util.createCommentRatingDto(0L, 0L, 1);
        final boolean isDeleted = commentDao.deleteRatingFromComment(testRating);
        assertFalse(isDeleted);
    }

    @Test
    void getTotalRatingOnComment() {
        final String testName = "GetTotalRatingOnCommentTest";
        final Integer rate = -1;
        final Integer rate2 = 1;
        final Integer rate3 = 1;
        final AuthUserDto authorUser = util.addTestAuthUserToDB(testName);

        final NewsDto testNews = util.addTestNewsToDB(testName, authorUser);

        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);
        final Long commentId = testComment.getId();

        final AuthUserDto testUser = util.addTestAuthUserToDB(testName + "1");
        final AuthUserDto testUser2 = util.addTestAuthUserToDB(testName + "2");
        final AuthUserDto testUser3 = util.addTestAuthUserToDB(testName + "3");
        util.addTestCommentRatingToDB(testUser.getId(), commentId, rate);
        util.addTestCommentRatingToDB(testUser2.getId(), commentId, rate2);
        util.addTestCommentRatingToDB(testUser3.getId(), commentId, rate3);


        final int totalRatingFromDB = commentDao.getTotalRatingOnComment(commentId);
        final int expectedTotalRating = rate + rate2 + rate3;
        assertEquals(expectedTotalRating, totalRatingFromDB);
    }

    @Test
    void totalRatingZero() {
        final String testName = "GetTotalRatingZeroOnCommentTest";
        final AuthUserDto testUser = util.addTestAuthUserToDB(testName);
        final NewsDto testNews = util.addTestNewsToDB(testName, testUser);
        final CommentDto testComment = util.addTestCommentToDB(testName, testNews);

        final int totalRatingFromDB = commentDao.getTotalRatingOnComment(testComment.getId());
        assertEquals(0, totalRatingFromDB);
    }
}