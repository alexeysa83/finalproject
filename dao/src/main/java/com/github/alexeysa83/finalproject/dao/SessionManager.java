package com.github.alexeysa83.finalproject.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public abstract class SessionManager {

    private final SessionFactory factory;

    public SessionManager(SessionFactory factory) {
        this.factory = factory;
    }

    public Session getSession() {
        return factory.openSession();
    }
}
