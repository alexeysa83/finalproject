package com.github.alexeysa83.finalproject.service;

import com.github.alexeysa83.finalproject.model.AuthUser;

public interface SecurityService {

    AuthUser createAndSaveAuthUser (String login, String password, String role);

    AuthUser login (String login, String password);

    boolean checkLoginIsTaken (String login);
}
