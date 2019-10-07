package com.github.alexeysa83.finalproject.dao;

public interface DAO<T> {

    T createAndSave(T t);

//    T getById(long id);

    boolean update(T t);

    boolean delete(long id);
}
