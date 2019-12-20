package com.github.alexeysa83.finalproject.service.comment;

import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.CommentRatingDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DefaultCommentService implements CommentService {

    private final CommentBaseDao commentDao;

    public DefaultCommentService(CommentBaseDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    @Transactional
    public CommentDto createComment(CommentDto comment) {
        return commentDao.add(comment);
    }

    @Override
    @Transactional
    public List<CommentDto> getCommentsOnNews(Long newsId) {
        return commentDao.getCommentsOnNews(newsId);
    }

    @Override
    @Transactional
    public boolean updateComment(CommentDto comment) {
        return commentDao.update(comment);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long id) {
        return commentDao.delete(id);
    }

    @Override
    @Transactional
    public boolean addRatingOnComment(CommentRatingDto ratingDto) {
        return commentDao.addRatingOnComment(ratingDto);
    }

    @Override
    @Transactional
    public boolean deleteRatingFromComment(CommentRatingDto ratingDto) {
        return commentDao.deleteRatingFromComment(ratingDto);
    }
}
