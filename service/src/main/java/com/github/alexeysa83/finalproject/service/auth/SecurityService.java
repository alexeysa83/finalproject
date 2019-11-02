package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;

public interface SecurityService {

    AuthUserDto createAuthUser(String login, String password);

    AuthUserDto getById (long id);

    AuthUserDto loginAuthUser(AuthUserDto user);

    boolean checkLoginIsTaken (String login);

    boolean updateAuthUser(AuthUserDto user);

    boolean deleteUser(long id);
}
