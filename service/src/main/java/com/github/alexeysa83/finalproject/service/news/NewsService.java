package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public interface NewsService {

    NewsDto createAndSave (NewsDto news);

    NewsDto getNewsOnId (long id);

    int getNewsTotalPages ();

    List<NewsDto> getNewsOnCurrentPage(int page);

    boolean updateNews (NewsDto news);

    boolean deleteNews (long id);
}
