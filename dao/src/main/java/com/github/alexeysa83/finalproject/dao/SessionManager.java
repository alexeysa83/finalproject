package com.github.alexeysa83.finalproject.dao;

import org.hibernate.Session;

import javax.persistence.EntityManagerFactory;

public abstract class SessionManager<T> implements BaseDao<T> {

    private final EntityManagerFactory factory;

    public SessionManager(EntityManagerFactory factory) {
        this.factory = factory;
    }

    public Session getSession() {
        return factory.createEntityManager().unwrap(Session.class);
    }
}
