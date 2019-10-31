package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;

import java.util.List;

public interface NewsBaseDao extends BaseDao<NewsDto> {

    NewsDto createAndSave(NewsDto newsDto);

    int getRowsNews ();

    // add page parameter
    List<NewsDto> getNewsOnPage (int page, int pageSize);

    boolean delete (long id);
}
