package com.github.alexeysa83.finalproject.service.auth;

import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;

public interface AuthUserService {

    AuthUserDto createAuthUserAndUserInfo(String login, String password);

    AuthUserDto getById (Long id);

    AuthUserDto loginAuthUser(AuthUserDto user);

    boolean checkLoginIsTaken (String login);

    boolean updateAuthUser(AuthUserDto user);

    boolean deleteUser(Long id);
}
