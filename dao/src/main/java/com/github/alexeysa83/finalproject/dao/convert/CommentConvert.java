package com.github.alexeysa83.finalproject.dao.convert;

import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;

public  abstract class CommentConvert {

    private CommentConvert() {

    }

    public static CommentDto toDto(CommentEntity commentEntity) {
        if (commentEntity == null) {
            return null;
        }
        final CommentDto commentDto = new CommentDto();
        commentDto.setId(commentEntity.getId());
        commentDto.setContent(commentEntity.getContent());
        commentDto.setCreationTime(commentEntity.getCreationTime());
        commentDto.setAuthId(commentEntity.getAuthUser().getId());
        commentDto.setNewsId(commentEntity.getNews().getId());
        commentDto.setAuthorComment(commentEntity.getAuthUser().getLogin());

        return commentDto;
    }

    public static CommentEntity toEntity(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        final CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentDto.getId());
        commentEntity.setContent(commentDto.getContent());
        commentEntity.setCreationTime(commentDto.getCreationTime());
        return commentEntity;
    }
}
