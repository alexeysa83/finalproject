package com.github.alexeysa83.finalproject.service;

import com.github.alexeysa83.finalproject.model.News;

import java.util.List;

public interface NewsService {

    List<News> getNewsOnPage();

    News createAndSave (News news);
}
