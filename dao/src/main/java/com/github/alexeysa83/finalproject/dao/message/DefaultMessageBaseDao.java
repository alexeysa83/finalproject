package com.github.alexeysa83.finalproject.dao.message;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.model.dto.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultMessageBaseDao implements MessageBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultMessageBaseDao.class);
    private DataSource mysql = DataSource.getInstance();

    private static volatile MessageBaseDao instance;

    public static MessageBaseDao getInstance() {
        MessageBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (MessageBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultMessageBaseDao();
                }
            }
        }
        return localInstance;
    }


    @Override
    public MessageDto createAndSave(MessageDto messageDto) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("insert into message (content, creation_time, auth_id, news_id) values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, messageDto.getContent());
            statement.setTimestamp(2, messageDto.getCreationTime());
            statement.setLong(3, messageDto.getAuthId());
            statement.setLong(4, messageDto.getNewsId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                final boolean isSaved = generatedKeys.next();
                if (!isSaved) {
                    return null;
                }
                final long id = generatedKeys.getLong(1);
                log.info("Message id: {} saved to DB at: {}", id, LocalDateTime.now());
                return new MessageDto
                        (id, messageDto.getContent(), messageDto.getCreationTime(),
                                messageDto.getAuthId(), messageDto.getNewsId(), messageDto.getAuthorMessage());
            }

        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public MessageDto getById(long id) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select m.content, m.creation_time, m.auth_id, m.news_id, au.login from message m " +
                             "join auth_user au on m.auth_id = au.id where m.id = ?")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                final boolean isExist = resultSet.next();
                if (!isExist) {
                    return null;
                }
                final String content = resultSet.getString("content");
                final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                final long authId = resultSet.getLong("auth_id");
                final long newsId = resultSet.getLong("news_id");
                final String login = resultSet.getString("login");
                return new MessageDto(id, content, creationTime, authId, newsId, login);
            }
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<MessageDto> getMessagesOnNews(long newsId) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select m.id, m.content, m.creation_time, m.auth_id, m.news_id, au.login from message m " +
                            "join auth_user au on m.auth_id = au.id where m.news_id = ?")) {
                statement.setLong(1, newsId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    final List<MessageDto> messageDtoList = new ArrayList<>();
                    while (resultSet.next()) {
                        final long id = resultSet.getLong("id");
                        final String content = resultSet.getString("content");
                        final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                        final long authId = resultSet.getLong("auth_id");
                        final String login = resultSet.getString("login");
                        messageDtoList.add(new MessageDto(id, content, creationTime, authId, newsId, login));
                    }
                    connection.commit();
                    return messageDtoList;
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                log.error("Unable to get list of messages from DB at: {}", LocalDateTime.now());
            } catch (SQLException ex) {
                log.error("Unable to rollback transaction at: {}", LocalDateTime.now(), ex);
                throw new RuntimeException(ex);
            }
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.error("Unable to close connection at: {}", LocalDateTime.now(), e);
                }
            }
        }
    }

    @Override
    public boolean update(MessageDto messageDto) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update message set content = ? where id = ?")) {
            statement.setString(1, messageDto.getContent());
            statement.setLong(2, messageDto.getId());
            log.info("Message id: {} updated in DB at: {}", messageDto.getId(), LocalDateTime.now());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("delete from message where id = ?")) {
                        statement.setLong(1, id);
            log.info("Message id: {} deleted from DB at: {}", id, LocalDateTime.now());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }
}
