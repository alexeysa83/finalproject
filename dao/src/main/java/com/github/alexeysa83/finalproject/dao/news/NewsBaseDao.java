package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.News;

import java.util.List;

public interface NewsBaseDao extends BaseDao<News> {

    News createAndSave(News news);

    // add page parameter
    List<News> getNewsOnPage ();

    boolean delete (long id);
}
