package com.github.alexeysa83.finalproject.dao;

public interface BaseDao<T> {

    T add (T t);

    T getById (Long id);

    boolean update(T t);

    boolean delete (Long id);
}
