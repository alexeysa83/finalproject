package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.NewsRatingDto;

import java.util.List;

public interface NewsBaseDao extends BaseDao<NewsDto> {

    int getRows();

    List<NewsDto> getNewsOnPage(int page, int pageSize);

    boolean addRatingOnNews(NewsRatingDto ratingDto);

    boolean deleteRatingFromNews(NewsRatingDto ratingDto);

   Integer getRatingOnNewsFromUser(Long id, Long newsId);

    int getTotalRatingOnNews(Long newsId);
}
