package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.UserDto;

public abstract class ConvertEntityDTO {

    private ConvertEntityDTO() {
    }

    public static AuthUserDto AuthUserToDto(AuthUserEntity authUserEntity) {
        if (authUserEntity == null) {
            return null;
        }
        final AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(authUserEntity.getId());
        authUserDto.setLogin(authUserEntity.getLogin());
        authUserDto.setPassword(authUserEntity.getPassword());
        authUserDto.setRole(authUserEntity.getRole());
        authUserDto.setBlocked(authUserEntity.isBlocked());

        final UserDto userDto = UserToDto(authUserEntity.getUser());
        authUserDto.setUserDto(userDto);
        return authUserDto;
    }

    public static AuthUserEntity AuthUserToEntity(AuthUserDto authUserDto) {
        if (authUserDto == null) {
            return null;
        }
        final AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setId(authUserDto.getId());
        authUserEntity.setLogin(authUserDto.getLogin());
        authUserEntity.setPassword(authUserDto.getPassword());
        authUserEntity.setRole(authUserDto.getRole());
        authUserEntity.setBlocked(authUserDto.isBlocked());

        final UserEntity userEntity = UserToEntity(authUserDto.getUserDto());
        if (userEntity != null) {
            userEntity.setAuthUser(authUserEntity);
        }
        authUserEntity.setUser(userEntity);
        return authUserEntity;
    }

    public static UserDto UserToDto(UserEntity userEntity) {
        if (userEntity == null) {
            return null;
        }
        final UserDto userDto = new UserDto();
        userDto.setAuthId(userEntity.getAuthId());
        userDto.setFirstName(userEntity.getFirstName());
        userDto.setLastName(userEntity.getLastName());
        userDto.setRegistrationTime(userEntity.getRegistrationTime());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhone(userEntity.getPhone());
        userDto.setUserLogin(userEntity.getAuthUser().getLogin());

        return userDto;
    }

    public static UserEntity UserToEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        final UserEntity userEntity = new UserEntity();
        userEntity.setAuthId(userDto.getAuthId());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setRegistrationTime(userDto.getRegistrationTime());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPhone(userDto.getPhone());

        return userEntity;
    }

    public static NewsDto NewsToDto(NewsEntity newsEntity) {
        if (newsEntity == null) {
            return null;
        }
        final NewsDto newsDto = new NewsDto();
        newsDto.setId(newsEntity.getId());
        newsDto.setTitle(newsEntity.getTitle());
        newsDto.setContent(newsEntity.getContent());
        newsDto.setCreationTime(newsEntity.getCreationTime());
        newsDto.setAuthId(newsEntity.getAuthUser().getId());
        newsDto.setAuthorNews(newsEntity.getAuthUser().getLogin());

        return newsDto;
    }

    public static NewsEntity NewsToEntity(NewsDto newsDto) {
        if (newsDto == null) {
            return null;
        }
        final NewsEntity newsEntity = new NewsEntity();
        newsEntity.setTitle(newsDto.getTitle());
        newsEntity.setContent(newsDto.getContent());
        newsEntity.setCreationTime(newsDto.getCreationTime());
        return newsEntity;
    }

}
