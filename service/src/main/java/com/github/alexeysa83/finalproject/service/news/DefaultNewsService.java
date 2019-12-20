package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.NewsRatingDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DefaultNewsService implements NewsService {

    // In service page size select
    private final int PAGE_SIZE = 10;

    // Colours for rating news
    private final String GREEN_COLOUR = "#28fc34";
    private final String RED_COLOUR = "red";
    private final String BLACK_COLOUR = "black";


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
        final NewsDto newsFromDB = newsDao.getById(id);
        setColourToRating(newsFromDB);
        return newsFromDB;
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
        final List<NewsDto> newsOnPage = newsDao.getNewsOnPage(page, PAGE_SIZE);
        newsOnPage.forEach(this::setColourToRating);
        return newsOnPage;
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

    @Override
    @Transactional
    public boolean addRatingOnNews(NewsRatingDto ratingDto) {
        return newsDao.addRatingOnNews(ratingDto);
    }

    @Override
    @Transactional
    public boolean deleteRatingFromNews(NewsRatingDto ratingDto) {
        return newsDao.deleteRatingFromNews(ratingDto);
    }

    private void setColourToRating(NewsDto newsDto) {
        final int ratingTotal = newsDto.getRatingTotal();
        if (ratingTotal > 0) {
            newsDto.setRatingColour(GREEN_COLOUR);
        } else if (ratingTotal < 0) {
            newsDto.setRatingColour(RED_COLOUR);
        } else {
            newsDto.setRatingColour(BLACK_COLOUR);
        }
    }
}
