package com.github.alexeysa83.finalproject.service.impl;

import com.github.alexeysa83.finalproject.dao.NewsDao;
import com.github.alexeysa83.finalproject.dao.impl.DefaultNewsDao;
import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.NewsService;

import java.util.List;

public class DefaultNewsService implements NewsService {

    private NewsDao newsDao = DefaultNewsDao.getInstance();

    private static volatile NewsService instance;

    public static NewsService getInstance() {
        NewsService localInstance = instance;
        if (localInstance == null) {
            synchronized (NewsService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultNewsService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public News createAndSave(News news) {
        return newsDao.createAndSave(news);
    }

    //Add page parameter
    @Override
    public List<News> getNewsOnPage() {
        return newsDao.getNewsOnPage();
    }
}
