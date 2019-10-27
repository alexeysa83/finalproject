package com.github.alexeysa83.finalproject.dao.comment;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;

import java.util.List;

public interface CommentBaseDao extends BaseDao <CommentDto> {

    CommentDto createAndSave(CommentDto comment);

    List<CommentDto> getCommentsOnNews(long newsId);

    boolean delete (long id);
}
