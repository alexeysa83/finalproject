package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.CommentRatingDto;

import java.util.List;

public interface CommentBaseDao extends BaseDao<CommentDto> {

    List<CommentDto> getCommentsOnNews(Long newsId);

    boolean addRatingOnComment(CommentRatingDto ratingDto);

    boolean deleteRatingFromComment(CommentRatingDto ratingDto);

    Integer getRatingOnCommentFromUser(Long authId, Long commentId);

    int getTotalRatingOnComment(Long commentId);
}
