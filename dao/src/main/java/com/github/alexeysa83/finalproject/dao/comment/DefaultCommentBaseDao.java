package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.Utils.DaoAuthenticationUtils;
import com.github.alexeysa83.finalproject.dao.convert_entity.CommentConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.repository.CommentRepository;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.CommentRatingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            final CommentDto commentDto = CommentConvert.toDto(commentEntity);
            return addRatingFieldsToCommentDto(commentDto);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<CommentDto> getCommentsOnNews(Long newsId) {
        List<CommentDto> commentDtoList;
        final NewsEntity newsEntity = commentRepository.getNewsById(newsId);
        List<CommentEntity> entityComments = newsEntity.getComments();
        commentDtoList = entityComments.stream()
                .map(CommentConvert::toDto)
                .map(this::addRatingFieldsToCommentDto)
                .collect(Collectors.toList());
        return commentDtoList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    CommentDto addRatingFieldsToCommentDto(CommentDto commentDto) {
        final Long userInSessionAuthId = DaoAuthenticationUtils.getPrincipalUserAuthId();
        final Long commentId = commentDto.getId();
        final Integer rate = getRatingOnCommentFromUser(userInSessionAuthId, commentId);
        final int totalRatingOnComment = getTotalRatingOnComment(commentId);

        commentDto.setUserInSessionRateOnThisComment(rate);
        commentDto.setRatingTotal(totalRatingOnComment);
        return commentDto;
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
        commentRepository.deleteAllRatingOnComment(id);
        final int rowsDeleted = commentRepository.deleteComment(id);
        if (rowsDeleted > 0) {
            log.info("Comment id: {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete comment from in DB: {}, at: {}", id, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }

    /**
     * COMMENT RATING
     */

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean addRatingOnComment(CommentRatingDto ratingDto) {
        final Long authId = ratingDto.getAuthId();
        final Long commentId = ratingDto.getCommentId();
        final int rowsUpdated = commentRepository.addRatingOnComment(authId, commentId, ratingDto.getRate());
        if (rowsUpdated > 0) {
            log.info("Rating on comment id: {} from user id: {} added to DB at: {}", commentId, authId, LocalDateTime.now());
        } else {
            log.info("Failed to add rating: {} on comment at: {}", ratingDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean deleteRatingFromComment(CommentRatingDto ratingDto) {
        final Long authId = ratingDto.getAuthId();
        final Long commentId = ratingDto.getCommentId();
        final int rowsDeleted = commentRepository.deleteSingleRatingOnComment(authId, commentId);
        if (rowsDeleted > 0) {
            log.info("Rating on comment id: {} from user id: {} deleted from DB at: {}", commentId, authId, LocalDateTime.now());
        } else {
            log.info("Failed to delete rating: {} on comment at: {}", ratingDto, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getRatingOnCommentFromUser(Long authId, Long commentId) {
        Integer ratingOnCommentFromUser;
        try {
            ratingOnCommentFromUser = commentRepository.getRatingOnCommentFromUser(authId, commentId);
        } catch (NullPointerException e) {
            return null;
        }
        return ratingOnCommentFromUser;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getTotalRatingOnComment(Long commentId) {
        final Integer totalRatingOnComment = commentRepository.getTotalRatingOnComments(commentId);
        return (totalRatingOnComment == null) ? 0 : totalRatingOnComment;
    }
}
