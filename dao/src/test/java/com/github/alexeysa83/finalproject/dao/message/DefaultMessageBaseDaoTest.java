package com.github.alexeysa83.finalproject.dao.message;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.dao.authuser.AuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.authuser.DefaultAuthUserBaseDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.MessageDto;
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

class DefaultMessageBaseDaoTest {

    private final static AuthUserBaseDao authUserDao = DefaultAuthUserBaseDao.getInstance();
    private final static NewsBaseDao newsDao = DefaultNewsBaseDao.getInstance();
    private MessageBaseDao messageDao = DefaultMessageBaseDao.getInstance();
    private static DataSource mysql = DataSource.getInstance();
    private final static AuthUserDto testUser;
    private final static NewsDto testNews;


    static {
        testUser = authUserDao.createAndSave(new AuthUserDto("MessageTest", "Pass"), new Timestamp(System.currentTimeMillis()));
        testNews = newsDao.createAndSave(new NewsDto
                ("MessageTest", "MessageTest", new Timestamp(System.currentTimeMillis()), testUser.getId(), null));
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
        final MessageDto testMessage = new MessageDto
                ("MessageCreate", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final MessageDto savedMessage = messageDao.createAndSave(testMessage);
        assertNotNull(savedMessage);

        final Long id = savedMessage.getId();
        assertNotNull(id);
        assertEquals(testMessage.getContent(), savedMessage.getContent());
        assertEquals(testMessage.getCreationTime(), savedMessage.getCreationTime());
        assertEquals(testMessage.getAuthId(), savedMessage.getAuthId());
        assertEquals(testMessage.getNewsId(), savedMessage.getNewsId());
        assertEquals(testMessage.getAuthorMessage(), savedMessage.getAuthorMessage());

        messageDao.delete(id);
    }

    @Test
    void getById() {
        final MessageDto message = new MessageDto
                ("MessageId", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final MessageDto testMessage = messageDao.createAndSave(message);
        final long id = testMessage.getId();
        final MessageDto messageFromDB = messageDao.getById(id);

        assertNotNull(messageFromDB);
        assertEquals(testMessage.getId(), messageFromDB.getId());
        assertEquals(testMessage.getContent(), messageFromDB.getContent());
        assertEquals(testMessage.getCreationTime(), messageFromDB.getCreationTime());
        assertEquals(testMessage.getAuthId(), messageFromDB.getAuthId());
        assertEquals(testMessage.getNewsId(), messageFromDB.getNewsId());
        assertEquals(testMessage.getAuthorMessage(), messageFromDB.getAuthorMessage());

        messageDao.delete(id);
    }

    @Test
    void getMessagesOnNews() {
        List<MessageDto> testList = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final MessageDto message = new MessageDto
                    ("MessageId" + i, getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
            final MessageDto m = messageDao.createAndSave(message);
            testList.add(m);
        }

        List<MessageDto> listFromDB = messageDao.getMessagesOnNews(testNews.getId());
        assertEquals(testList.size(), listFromDB.size());
        for (int i = 0; i < 10; i++) {
            final MessageDto testMessage = testList.get(i);
            final MessageDto messageFromDB = listFromDB.get(i);
            assertNotNull(messageFromDB);
            assertEquals(testMessage.getId(), messageFromDB.getId());
            assertEquals(testMessage.getContent(), messageFromDB.getContent());
            assertEquals(testMessage.getCreationTime(), messageFromDB.getCreationTime());
            assertEquals(testMessage.getAuthId(), messageFromDB.getAuthId());
            assertEquals(testMessage.getNewsId(), messageFromDB.getNewsId());
            assertEquals(testMessage.getAuthorMessage(), messageFromDB.getAuthorMessage());

            messageDao.delete(messageFromDB.getId());
        }
    }

    @Test
    void update() {
        final MessageDto message = new MessageDto
                ("MessageUpdate", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final MessageDto testMessage = messageDao.createAndSave(message);
        final long id = testMessage.getId();
        final MessageDto messageToUpdate = new MessageDto
                (id, "UpdateComplete", message.getCreationTime(),
                        message.getAuthId(), message.getNewsId(), message.getAuthorMessage());

        final boolean isUpdated = messageDao.update(messageToUpdate);
        assertTrue(isUpdated);

        final MessageDto afterUpdate = messageDao.getById(id);
        assertEquals(messageToUpdate.getContent(), afterUpdate.getContent());

        messageDao.delete(id);
    }

    @Test
    void delete() {
        final MessageDto message = new MessageDto
                ("MessageDelete", getTime(), testUser.getId(), testNews.getId(), testUser.getLogin());
        final MessageDto testMessage = messageDao.createAndSave(message);
        final long id = testMessage.getId();
        MessageDto messageToDelete = messageDao.getById(id);
        assertNotNull(messageToDelete);

        final boolean isDeleted = messageDao.delete(id);
        assertTrue(isDeleted);

        final MessageDto afterDelete = messageDao.getById(id);
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