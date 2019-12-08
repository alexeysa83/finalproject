package com.github.alexeysa83.finalproject.service.comment;

import com.github.alexeysa83.finalproject.model.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(CommentDto comment);

    List<CommentDto> getCommentsOnNews(Long newsId);

    boolean updateComment(CommentDto comment);

    boolean deleteComment(Long id);
}
