package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public class DefaultNewsService implements NewsService {

    private NewsBaseDao newsDao = DefaultNewsBaseDao.getInstance();

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
    public NewsDto createAndSave(NewsDto news) {
        return newsDao.createAndSave(news);
    }

    @Override
    public NewsDto getNewsOnId(long id) {
        return newsDao.getById(id);
    }

    //Add page parameter
    @Override
    public List<NewsDto> getNewsOnPage() {
        return newsDao.getNewsOnPage();
    }

    @Override
    public boolean updateNews(NewsDto news) {
        return newsDao.update(news);
    }

    @Override
    public boolean deleteNews(long id) {
        return newsDao.delete(id);
    }
}
