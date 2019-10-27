package com.github.alexeysa83.finalproject.service.comment;

import com.github.alexeysa83.finalproject.model.dto.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createAndSave (CommentDto comment);

    List<CommentDto> getCommentsOnNews(long newsId);

    boolean updateComment(CommentDto comment);

    boolean deleteComment(long id);
}
