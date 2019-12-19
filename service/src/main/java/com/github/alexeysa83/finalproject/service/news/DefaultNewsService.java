package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DefaultNewsService implements NewsService {

    // In service page size select
    private final int PAGE_SIZE = 10;

    private final NewsBaseDao newsDao;

    public DefaultNewsService(NewsBaseDao newsDao) {
        this.newsDao = newsDao;
    }

    @Override
    @Transactional
    public NewsDto createNews(NewsDto news) {
        return newsDao.add(news);
    }

    @Override
    @Transactional
    public NewsDto getNewsOnId(Long id) {
        return newsDao.getById(id);
    }

    @Override
    @Transactional
    public int getNewsTotalPages() {
        int rowsNews = newsDao.getRows();
        int totalPages = rowsNews / PAGE_SIZE;
        if (rowsNews % PAGE_SIZE > 0) {
            totalPages++;
        }
        return totalPages;
    }

    //Add page parameter
    @Override
    @Transactional
        public List<NewsDto> getNewsOnCurrentPage(int page) {
        return newsDao.getNewsOnPage(page, PAGE_SIZE);
    }

    @Override
    @Transactional
    public boolean updateNews(NewsDto news) {
        return newsDao.update(news);
    }

    @Override
    @Transactional
    public boolean deleteNews(Long id) {
        return newsDao.delete(id);
    }
}
