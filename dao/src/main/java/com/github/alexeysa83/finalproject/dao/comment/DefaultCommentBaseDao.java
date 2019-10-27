package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultCommentBaseDao implements CommentBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultCommentBaseDao.class);
    private DataSource mysql = DataSource.getInstance();

    private static volatile CommentBaseDao instance;

    public static CommentBaseDao getInstance() {
        CommentBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (CommentBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultCommentBaseDao();
                }
            }
        }
        return localInstance;
    }


    @Override
    public CommentDto createAndSave(CommentDto comment) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("insert into comment (content, creation_time, auth_id, news_id) values (?, ?, ?, ?)",
                             Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, comment.getContent());
            statement.setTimestamp(2, comment.getCreationTime());
            statement.setLong(3, comment.getAuthId());
            statement.setLong(4, comment.getNewsId());
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                final boolean isSaved = generatedKeys.next();
                if (!isSaved) {
                    return null;
                }
                final long id = generatedKeys.getLong(1);
                log.info("Comment id: {} saved to DB at: {}", id, LocalDateTime.now());
                return new CommentDto
                        (id, comment.getContent(), comment.getCreationTime(),
                                comment.getAuthId(), comment.getNewsId(), comment.getAuthorComment());
            }

        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommentDto getById(long id) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("select c.content, c.creation_time, c.auth_id, c.news_id, au.login from comment c " +
                             "join auth_user au on c.auth_id = au.id where c.id = ?")) {
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
                return new CommentDto(id, content, creationTime, authId, newsId, login);
            }
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CommentDto> getCommentsOnNews(long newsId) {
        Connection connection = null;
        try {
            connection = mysql.getConnection();
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement
                    ("select c.id, c.content, c.creation_time, c.auth_id, c.news_id, au.login from comment c " +
                            "join auth_user au on c.auth_id = au.id where c.news_id = ?")) {
                statement.setLong(1, newsId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    final List<CommentDto> commentList = new ArrayList<>();
                    while (resultSet.next()) {
                        final long id = resultSet.getLong("id");
                        final String content = resultSet.getString("content");
                        final Timestamp creationTime = resultSet.getTimestamp("creation_time");
                        final long authId = resultSet.getLong("auth_id");
                        final String login = resultSet.getString("login");
                        commentList.add(new CommentDto(id, content, creationTime, authId, newsId, login));
                    }
                    connection.commit();
                    return commentList;
                }
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
                log.error("Unable to get list of comments from DB at: {}", LocalDateTime.now());
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
    public boolean update(CommentDto comment) {
        try (Connection connection = mysql.getConnection();
             PreparedStatement statement = connection.prepareStatement
                     ("update comment set content = ? where id = ?")) {
            statement.setString(1, comment.getContent());
            statement.setLong(2, comment.getId());
            log.info("Comment id: {} updated in DB at: {}", comment.getId(), LocalDateTime.now());
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
                     ("delete from comment where id = ?")) {
                        statement.setLong(1, id);
            log.info("Comment id: {} deleted from DB at: {}", id, LocalDateTime.now());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("SQLException at: {}", LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }
}
