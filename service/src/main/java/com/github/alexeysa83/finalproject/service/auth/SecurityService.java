package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.model.AuthUser;

public interface SecurityService {

    AuthUser createAndSaveAuthUser (AuthUser user);

//    AuthUser getByLogin (String login);

    AuthUser getById (String id);

    AuthUser login (AuthUser user);

    boolean checkLoginIsTaken (String login);

    boolean update (AuthUser user);

    boolean delete (String id);
}
