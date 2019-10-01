package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.dao.impl.DefaultAuthUserDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import org.junit.Test;

import static org.junit.Assert.*;


public class DefaultAuthUserDaoTest {

    private final AuthUserDao authUserDao = DefaultAuthUserDao.getInstance();

    @Test
    public void getInstance() {
        assertNotNull(authUserDao);
    }

    @Test
    public void saveAuthUser() {
        authUserDao.saveAuthUser(new AuthUser("testSave", null, null));
        assertNotNull(authUserDao.getAuthUserByLogin("testSave"));
    }

    @Test
    public void createAuthUser() {
        authUserDao.createAuthUser("testLogin", "testPassword", false);
        assertNotNull(authUserDao.getAuthUserByLogin("testLogin"));
    }

    @Test
    public void getAuthUserByLogin() {
        assertNotNull(authUserDao.getAuthUserByLogin("admin"));
    }

    @Test
    public void checkLoginIsTaken() {
        assertEquals("true", String.valueOf(authUserDao.checkLoginIsTaken("user")));
        assertEquals("false", String.valueOf(authUserDao.checkLoginIsTaken("valid")));

    }
}