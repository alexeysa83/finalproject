package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public interface NewsBaseDao extends BaseDao<NewsDto> {

    NewsDto add(NewsDto newsDto);

    int getRows();

    List<NewsDto> getNewsOnPage (int page, int pageSize);

    boolean delete (long id);
}
