package com.github.alexeysa83.finalproject.dao;

public interface BaseDao<T> {

    T getById (long id);

    boolean update(T t);

}
