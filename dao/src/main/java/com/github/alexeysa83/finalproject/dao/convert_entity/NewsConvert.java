package com.github.alexeysa83.finalproject.dao.convert_entity;

import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsConvert {

    private NewsConvert() {
    }

    public static NewsDto toDto(NewsEntity newsEntity) {
        if (newsEntity == null) {
            return null;
        }
        final NewsDto newsDto = new NewsDto();
        newsDto.setId(newsEntity.getId());
        newsDto.setTitle(newsEntity.getTitle());
        newsDto.setContent(newsEntity.getContent());
        newsDto.setCreationTime(newsEntity.getCreationTime());
        newsDto.setAuthId(newsEntity.getAuthUser().getId());
        newsDto.setAuthorNews(newsEntity.getAuthUser().getLogin());

        final List<CommentEntity> entityComments = newsEntity.getComments();
        if (entityComments.size() > 0) {
            final List<CommentDto> commentDtos = new ArrayList<>();
            entityComments.forEach(commentEntity -> {
                final CommentDto commentDto = CommentConvert.toDto(commentEntity);
                commentDtos.add(commentDto);
            });
            newsDto.setComments(commentDtos);
        }
        return newsDto;
    }

    public static NewsEntity toEntity(NewsDto newsDto) {
        if (newsDto == null) {
            return null;
        }
        final NewsEntity newsEntity = new NewsEntity();
        newsEntity.setId(newsDto.getId());
        newsEntity.setTitle(newsDto.getTitle());
        newsEntity.setContent(newsDto.getContent());
        newsEntity.setCreationTime(newsDto.getCreationTime());
        return newsEntity;
    }
}
