package com.github.alexeysa83.finalproject.service.comment;

import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.CommentRatingDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DefaultCommentService implements CommentService {

    // Colours for rating comments
    private final String GREEN_COLOUR = "#28fc34";
    private final String RED_COLOUR = "red";
    private final String BLACK_COLOUR = "black";

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
        final List<CommentDto> commentsOnNews = commentDao.getCommentsOnNews(newsId);
        commentsOnNews.forEach(this::setColourToRating);
        return commentsOnNews;
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

    private void setColourToRating(CommentDto commentDto) {
        final int ratingTotal = commentDto.getRatingTotal();
        if (ratingTotal > 0) {
            commentDto.setRatingColour(GREEN_COLOUR);
        } else if (ratingTotal < 0) {
            commentDto.setRatingColour(RED_COLOUR);
        } else {
            commentDto.setRatingColour(BLACK_COLOUR);
        }
    }
}
