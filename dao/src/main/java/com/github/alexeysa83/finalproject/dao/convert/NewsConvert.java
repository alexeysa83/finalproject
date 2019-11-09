package com.github.alexeysa83.finalproject.dao.convert;

import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;

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
