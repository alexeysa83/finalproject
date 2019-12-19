package com.github.alexeysa83.finalproject.web.request_entity;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import javax.validation.constraints.Size;

public class NewsRequestModel {

    @Size (min = 1, message = "invalid.news.title" )
    private String title;
    @Size (min = 1, message = "invalid.news.content" )
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NewsDto convertToNewsDto () {
        final NewsDto newsDto = new NewsDto();
        newsDto.setTitle(title);
        newsDto.setContent(content);
        return newsDto;
    }
}
