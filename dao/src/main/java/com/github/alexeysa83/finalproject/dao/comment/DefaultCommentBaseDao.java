package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.convert_entity.CommentConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.repository.CommentRepository;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class DefaultCommentBaseDao implements CommentBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultCommentBaseDao.class);

    private final CommentRepository commentRepository;

    public DefaultCommentBaseDao(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    /**
     *

     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public CommentDto add(CommentDto commentDto) {
        final AuthUserEntity authUserEntity = commentRepository.getAuthUserById(commentDto.getAuthId());
        final NewsEntity newsEntity = commentRepository.getNewsById(commentDto.getNewsId());
        final CommentEntity commentEntity = CommentConvert.toEntity(commentDto);
        commentEntity.setAuthUser(authUserEntity);
        commentEntity.setNews(newsEntity);

        authUserEntity.getComments().add(commentEntity);
        newsEntity.getComments().add(commentEntity);

        final CommentEntity savedComment = commentRepository.save(commentEntity);
        if (savedComment != null) {
            log.info("Comment id: {} saved to DB at: {}", commentEntity.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to save new comment to DB: {}, at: {}", commentDto, LocalDateTime.now());
        }
        return CommentConvert.toDto(savedComment);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public CommentDto getById(Long id) {
        final Optional<CommentEntity> optional = commentRepository.findById(id);
        if (optional.isPresent()) {
            final CommentEntity commentEntity = optional.get();
            return CommentConvert.toDto(commentEntity);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<CommentDto> getCommentsOnNews(Long newsId) {
        List<CommentDto> commentDtoList;
        final NewsEntity newsEntity = commentRepository.getNewsById(newsId);
        List<CommentEntity> entityComments = newsEntity.getComments();
        commentDtoList = entityComments.stream().map(CommentConvert::toDto).collect(Collectors.toList());
        return commentDtoList;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean update(CommentDto commentDto) {
        final int rowsUpdated = commentRepository.updateContentComment(commentDto.getId(), commentDto.getContent());
        if (rowsUpdated > 0) {
            log.info("Comment id: {} updated in DB at: {}", commentDto.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to update comment in DB: {}, at: {}", commentDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        final int rowsDeleted = commentRepository.deleteComment(id);
        if (rowsDeleted > 0) {
            log.info("Comment id: {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete comment from in DB: {}, at: {}", id, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }
}
