package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.dao.news.DefaultNewsBaseDao;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public class DefaultNewsService implements NewsService {

    //select count * from NewsEntity/page size + 1

    // In service page size select
    private final int PAGE_SIZE = 10;

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
        return newsDao.add(news);
    }

    @Override
    public NewsDto getNewsOnId(long id) {
        return newsDao.getById(id);
    }

    @Override
    public int getNewsTotalPages() {
        int rowsNews = newsDao.getRows();
        int totalPages = rowsNews/PAGE_SIZE;
        if (rowsNews%PAGE_SIZE > 0) {
            totalPages++;
        }
        return totalPages;
    }

    //Add page parameter
    @Override
    public List<NewsDto> getNewsOnCurrentPage(int page) {
        return newsDao.getNewsOnPage(page, PAGE_SIZE);
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
