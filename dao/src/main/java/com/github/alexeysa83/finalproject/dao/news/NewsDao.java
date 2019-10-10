package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.DAO;
import com.github.alexeysa83.finalproject.model.News;

import java.util.List;

public interface NewsDao extends DAO<News> {

    News createAndSave(News news);

    // add page parameter
    List<News> getNewsOnPage ();

    boolean delete (long id);
}
