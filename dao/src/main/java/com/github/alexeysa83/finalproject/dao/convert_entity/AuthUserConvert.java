package com.github.alexeysa83.finalproject.dao.convert_entity;

import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

public abstract class AuthUserConvert {

    private AuthUserConvert() {
    }

    public static AuthUserDto toDto(AuthUserEntity authUserEntity) {
        if (authUserEntity == null) {
            return null;
        }
        final AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setId(authUserEntity.getId());
        authUserDto.setLogin(authUserEntity.getLogin());
        authUserDto.setPassword(authUserEntity.getPassword());
        authUserDto.setRole(authUserEntity.getRole());
        authUserDto.setDeleted(authUserEntity.isDeleted());

        final UserInfoDto userInfoDto = UserInfoConvert.toDto(authUserEntity.getUser());
        authUserDto.setUserInfoDto(userInfoDto);
        return authUserDto;
    }

    public static AuthUserEntity toEntity(AuthUserDto authUserDto) {
        if (authUserDto == null) {
            return null;
        }
        final AuthUserEntity authUserEntity = new AuthUserEntity();
        authUserEntity.setId(authUserDto.getId());
        authUserEntity.setLogin(authUserDto.getLogin());
        authUserEntity.setPassword(authUserDto.getPassword());
        authUserEntity.setRole(authUserDto.getRole());
        authUserEntity.setDeleted(authUserDto.isDeleted());

//        final UserInfoEntity userInfoEntity = UserInfoConvert.toEntity(authUserDto.getUserInfoDto());
//        if (userInfoEntity != null) {
//            userInfoEntity.setAuthUser(authUserEntity);
//        }
//        authUserEntity.setUser(userInfoEntity);
        return authUserEntity;
    }
}
