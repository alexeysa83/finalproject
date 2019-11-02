package com.github.alexeysa83.finalproject.service.comment;

import com.github.alexeysa83.finalproject.dao.comment.DefaultCommentBaseDao;
import com.github.alexeysa83.finalproject.dao.comment.CommentBaseDao;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;

import java.util.List;

public class DefaultCommentService implements CommentService {

    private CommentBaseDao commentDao = DefaultCommentBaseDao.getInstance();

    private static volatile CommentService instance;

    public static CommentService getInstance() {
        CommentService localInstance = instance;
        if (localInstance == null) {
            synchronized (CommentService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultCommentService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public CommentDto createAndSave(CommentDto comment) {
        return commentDao.add(comment);
    }

    @Override
    public List<CommentDto> getCommentsOnNews(long newsId) {
        return commentDao.getCommentsOnNews(newsId);
    }

    @Override
    public boolean updateComment(CommentDto comment) {
        return commentDao.update(comment);
    }

    @Override
    public boolean deleteComment(long id) {
        return commentDao.delete(id);
    }
}
