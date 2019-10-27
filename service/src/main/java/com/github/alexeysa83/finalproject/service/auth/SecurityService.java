package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;

public interface SecurityService {

    AuthUserDto createAndSaveAuthUser (AuthUserDto user);

//    AuthUser getByLogin (String login);

    AuthUserDto getById (long id);

    AuthUserDto login (AuthUserDto user);

    boolean checkLoginIsTaken (String login);

    boolean update (AuthUserDto user);

    boolean delete (long id);
}
