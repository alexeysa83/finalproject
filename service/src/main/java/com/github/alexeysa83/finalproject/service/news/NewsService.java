package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.model.News;

import java.util.List;

public interface NewsService {

    News createAndSave (News news);

    News getNewsOnId (String id);

    List<News> getNewsOnPage();

    String updateNews (News news);

    String deleteNews (String id);
}
