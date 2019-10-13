package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.dao.authuser.AuthUserDao;
import com.github.alexeysa83.finalproject.model.AuthUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultSecurityServiceTest {

    @Mock
    AuthUserDao authUserDao;

    @InjectMocks
    DefaultSecurityService service;

//    @Test
//    void createAndSaveAuthUser() {
//    }

//    @Test
//    void getById() {
//    }

    @Test
    void login () {
        // Test login not exist case
        when (authUserDao.getByLogin("loginNotExist")).thenReturn(null);
        AuthUser testUser = new AuthUser("loginNotExist", "Pass");
        AuthUser userFromDB = service.login(testUser);
        assertNull(userFromDB);

        // Test login exist and password correct case
        when(authUserDao.getByLogin("loginExist"))
                .thenReturn(new AuthUser("loginExist", "passCorrect"));
        testUser = new AuthUser("loginExist", "passCorrect");
        userFromDB = service.login(testUser);
        assertNotNull(userFromDB);
        assertEquals(testUser.getLogin(), userFromDB.getLogin());
        assertEquals(testUser.getPassword(), userFromDB.getPassword());

        // Test login exist and password wrong case
        testUser = new AuthUser("loginExist", "passWrong");
        userFromDB = service.login(testUser);
        assertNull(userFromDB);
    }

//    @Test
//    void checkLoginIsTaken() {
//    }
//
//    @Test
//    void update() {
//    }
//
//    @Test
//    void delete() {
//    }
}