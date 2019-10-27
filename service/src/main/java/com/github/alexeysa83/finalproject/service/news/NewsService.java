package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public interface NewsService {

    NewsDto createAndSave (NewsDto news);

    NewsDto getNewsOnId (long id);

    List<NewsDto> getNewsOnPage();

    boolean updateNews (NewsDto news);

    boolean deleteNews (long id);
}
