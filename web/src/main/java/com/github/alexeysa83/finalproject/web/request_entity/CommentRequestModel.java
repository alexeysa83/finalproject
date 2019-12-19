package com.github.alexeysa83.finalproject.web.request_entity;

import com.github.alexeysa83.finalproject.model.dto.CommentDto;

import javax.validation.constraints.Size;

public class CommentRequestModel {

    @Size(min = 1, message = "invalid.comment.content" )
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public CommentDto convertToCommentDto () {
        final CommentDto commentDto = new CommentDto();
        commentDto.setContent(content);
        return commentDto;
    }
}
