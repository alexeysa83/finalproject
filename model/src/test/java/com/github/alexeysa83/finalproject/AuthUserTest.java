package com.github.alexeysa83.finalproject;

import org.junit.Test;

import static org.junit.Assert.*;

public class AuthUserTest {

    AuthUser user = new AuthUser ("alexey", "password", Role.ADMIN);

    @Test
    public void getLogin() {
        String login = user.getLogin();
        assertEquals("alexey", login);
    }

    @Test
    public void setLogin() {
        user.setLogin("AlexeY");
        String login = user.getLogin();
        assertEquals("AlexeY", login);
    }

    @Test
    public void getPassword() {
        String password = user.getPassword();
        assertEquals("password", password);
    }

    @Test
    public void setPassword() {
        user.setPassword("123aaa321");
        String password = user.getPassword();
        assertEquals("123aaa321", password);
    }

    @Test
    public void getRole() {
        Role role = user.getRole();
        assertEquals(Role.ADMIN, role);
    }

    @Test
    public void setRole() {
        user.setRole(Role.USER);
        Role role = user.getRole();
        assertEquals(Role.USER, role);
    }
}