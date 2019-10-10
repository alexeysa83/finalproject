package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.dao.news.NewsDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsDao;
import com.github.alexeysa83.finalproject.model.News;
import com.github.alexeysa83.finalproject.service.UtilsService;

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

    @Override
    public News getNewsOnId(String value) {
        final long id = UtilsService.stringToLong(value);
        return newsDao.getById(id);
    }

    //Add page parameter
    @Override
    public List<News> getNewsOnPage() {
        return newsDao.getNewsOnPage();
    }

    @Override
    public boolean updateNews(News news) {
        return newsDao.update(news);
    }

    @Override
    public boolean deleteNews(String value) {
        final long id = UtilsService.stringToLong(value);
        return newsDao.delete(id);
    }
}
