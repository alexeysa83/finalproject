package com.github.alexeysa83.finalproject.service.comment;

import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultCommentService implements CommentService {

    private final CommentBaseDao commentDao;

    public DefaultCommentService(CommentBaseDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public CommentDto createComment(CommentDto comment) {
        return commentDao.add(comment);
    }

    @Override
    public List<CommentDto> getCommentsOnNews(Long newsId) {
        return commentDao.getCommentsOnNews(newsId);
    }

    @Override
    public boolean updateComment(CommentDto comment) {
        return commentDao.update(comment);
    }

    @Override
    public boolean deleteComment(Long id) {
        return commentDao.delete(id);
    }
}
