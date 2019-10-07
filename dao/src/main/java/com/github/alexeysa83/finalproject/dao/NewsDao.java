package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.model.News;

import java.util.List;

public interface NewsDao extends DAO <News> {

    // add page parameter
    List<News> getNewsOnPage ();
}
