package com.github.alexeysa83.finalproject;

public interface SecurityService {

    void saveAuthUser (AuthUser authUser);

    AuthUser createAndSaveAuthUser (String login, String password, boolean isAdmin);

    AuthUser login (String login, String password);

    boolean checkLoginIsTaken (String login);
}
