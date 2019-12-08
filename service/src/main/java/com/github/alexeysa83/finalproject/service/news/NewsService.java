package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public interface NewsService {

    NewsDto createNews(NewsDto news);

    NewsDto getNewsOnId (Long id);

    int getNewsTotalPages ();

    List<NewsDto> getNewsOnCurrentPage(int page);

    boolean updateNews (NewsDto news);

    boolean deleteNews (Long id);
}
