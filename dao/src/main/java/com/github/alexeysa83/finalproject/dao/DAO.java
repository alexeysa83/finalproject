package com.github.alexeysa83.finalproject.dao;

public interface DAO<T> {

    T getById (long id);

    boolean update(T t);

}
