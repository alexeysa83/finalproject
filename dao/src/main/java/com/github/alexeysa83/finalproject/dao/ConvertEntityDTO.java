package com.github.alexeysa83.finalproject.dao;

import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.CommentDto;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

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
        authUserDto.setDeleted(authUserEntity.isDeleted());

        final UserInfoDto userInfoDto = UserToDto(authUserEntity.getUser());
        authUserDto.setUserInfoDto(userInfoDto);
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
        authUserEntity.setDeleted(authUserDto.isDeleted());

        final UserInfoEntity userInfoEntity = UserToEntity(authUserDto.getUserInfoDto());
        if (userInfoEntity != null) {
            userInfoEntity.setAuthUser(authUserEntity);
        }
        authUserEntity.setUser(userInfoEntity);
        return authUserEntity;
    }

    public static UserInfoDto UserToDto(UserInfoEntity userInfoEntity) {
        if (userInfoEntity == null) {
            return null;
        }
        final UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setAuthId(userInfoEntity.getAuthId());
        userInfoDto.setFirstName(userInfoEntity.getFirstName());
        userInfoDto.setLastName(userInfoEntity.getLastName());
        userInfoDto.setRegistrationTime(userInfoEntity.getRegistrationTime());
        userInfoDto.setEmail(userInfoEntity.getEmail());
        userInfoDto.setPhone(userInfoEntity.getPhone());
        userInfoDto.setUserLogin(userInfoEntity.getAuthUser().getLogin());

        return userInfoDto;
    }

    public static UserInfoEntity UserToEntity(UserInfoDto userInfoDto) {
        if (userInfoDto == null) {
            return null;
        }
        final UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setAuthId(userInfoDto.getAuthId());
        userInfoEntity.setFirstName(userInfoDto.getFirstName());
        userInfoEntity.setLastName(userInfoDto.getLastName());
        userInfoEntity.setRegistrationTime(userInfoDto.getRegistrationTime());
        userInfoEntity.setEmail(userInfoDto.getEmail());
        userInfoEntity.setPhone(userInfoDto.getPhone());

        return userInfoEntity;
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
        newsEntity.setId(newsDto.getId());
        newsEntity.setTitle(newsDto.getTitle());
        newsEntity.setContent(newsDto.getContent());
        newsEntity.setCreationTime(newsDto.getCreationTime());
        return newsEntity;
    }

    public static CommentDto CommentToDto (CommentEntity commentEntity) {
        if (commentEntity==null) {
            return null;
        }
        final CommentDto commentDto = new CommentDto();
        commentDto.setId(commentEntity.getId());
        commentDto.setContent(commentEntity.getContent());
        commentDto.setCreationTime(commentEntity.getCreationTime());
        commentDto.setAuthId(commentEntity.getAuthUser().getId());
        commentDto.setNewsId(commentEntity.getNews().getId());
        commentDto.setAuthorComment(commentEntity.getAuthUser().getLogin());

        return commentDto;
    }

    public static CommentEntity CommentToEntity (CommentDto commentDto) {
        if (commentDto==null) {
            return null;
        }
        final CommentEntity commentEntity = new CommentEntity();
        commentEntity.setId(commentDto.getId());
        commentEntity.setContent(commentDto.getContent());
        commentEntity.setCreationTime(commentDto.getCreationTime());
        return commentEntity;
    }
}
