package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.convert.CommentConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DefaultCommentBaseDao implements CommentBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultCommentBaseDao.class);

    /**
     * Optimization?
     */
    @Override
    public CommentDto add(CommentDto commentDto) {
        final CommentEntity commentEntity = CommentConvert.toEntity(commentDto);

        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            final AuthUserEntity authUserEntity = session.get(AuthUserEntity.class, commentDto.getAuthId());
            final NewsEntity newsEntity = session.get(NewsEntity.class, commentDto.getNewsId());
            commentEntity.setAuthUser(authUserEntity);
            commentEntity.setNews(newsEntity);
            session.save(commentEntity);
            session.getTransaction().commit();
            log.info("Comment id: {} saved to DB at: {}", commentEntity.getId(), LocalDateTime.now());
            return CommentConvert.toDto(commentEntity);
        } catch (PersistenceException | NullPointerException e) {
            log.error("Fail to save new comment to DB: {}, at: {}", commentDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public CommentDto getById(long id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        final CommentEntity commentEntity = session.get(CommentEntity.class, id);
        session.getTransaction().commit();
        session.close();
        return CommentConvert.toDto(commentEntity);
    }

    @Override
    public List<CommentDto> getCommentsOnNews(long newsId) {
        List<CommentDto> commentDtoList = new ArrayList<>();

        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();

            final NewsEntity newsEntity = session.get(NewsEntity.class, newsId);
            List<CommentEntity> entityComments = newsEntity.getComments();
            entityComments.forEach(commentEntity -> {
                CommentDto commentDto = CommentConvert.toDto(commentEntity);
                commentDtoList.add(commentDto);
            });
            session.getTransaction().commit();
            return commentDtoList;
        } catch (PersistenceException e) {
            log.error("Fail to get list of comments on news id: {},  at: {}", newsId, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    //    @Override
//    public List<CommentDto> getCommentsOnNews(long newsId) {
//        Connection connection = null;
//        try {
//            connection = mysql.getConnection();
//            connection.setAutoCommit(false);
//            try (PreparedStatement statement = connection.prepareStatement
//                    ("select c.id, c.content, c.creation_time, c.auth_id, c.news_id, au.login from comment c " +
//                            "join auth_user au on c.auth_id = au.id where c.news_id = ?")) {
//                statement.setLong(1, newsId);
//                try (ResultSet resultSet = statement.executeQuery()) {
//                    final List<CommentDto> commentList = new ArrayList<>();
//                    while (resultSet.next()) {
//                        final long id = resultSet.getLong("id");
//                        final String content = resultSet.getString("content");
//                        final Timestamp creationTime = resultSet.getTimestamp("creation_time");
//                        final long authId = resultSet.getLong("auth_id");
//                        final String login = resultSet.getString("login");
//                        commentList.add(new CommentDto(id, content, creationTime, authId, newsId, login));
//                    }
//                    connection.commit();
//                    return commentList;
//                }
//            }
//        } catch (SQLException e) {
//            try {
//                connection.rollback();
//                log.error("Unable to get list of comments from DB at: {}", LocalDateTime.now());
//            } catch (SQLException ex) {
//                log.error("Unable to rollback transaction at: {}", LocalDateTime.now(), ex);
//                throw new RuntimeException(ex);
//            }
//            log.error("SQLException at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        } finally {
//            if (connection != null) {
//                try {
//                    connection.close();
//                } catch (SQLException e) {
//                    log.error("Unable to close connection at: {}", LocalDateTime.now(), e);
//                }
//            }
//        }
//    }

    @Override
    public boolean update(CommentDto commentDto) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            final int i = session.createQuery("update CommentEntity c set c.content=:content where c.id=:id")
                    .setParameter("content", commentDto.getContent())
                    .setParameter("id", commentDto.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("Comment id: {} updated in DB at: {}", commentDto.getId(), LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update comment in DB: {}, at: {}", commentDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean delete(long id) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            final int i = session.createQuery("delete CommentEntity c where c.id=:id")
                    .setParameter("id", id)
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("Comment id: {} deleted from DB at: {}", id, LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to delete comment from in DB: {}, at: {}", id, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }
}
