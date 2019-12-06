package com.github.alexeysa83.finalproject.dao.convert_entity;

import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

import java.util.ArrayList;
import java.util.List;

public abstract class UserInfoConvert {

    private UserInfoConvert() {}

    public static UserInfoDto toDto(UserInfoEntity userInfoEntity) {
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

        List<BadgeEntity> badgeEntities = userInfoEntity.getBadges();
        if (badgeEntities.size() > 0) {
            final List<BadgeDto> badgeDtos = new ArrayList<>();
            badgeEntities.forEach(badgeEntity -> {
                final BadgeDto badgeDto = BadgeConvert.toDto(badgeEntity);
                badgeDtos.add(badgeDto);
            });
            userInfoDto.setBadges(badgeDtos);
        }
        return userInfoDto;
    }

    /**
     * Is used only in add method in DefaultUserInfoBaseDao
          */
    public static UserInfoEntity toEntity(UserInfoDto userInfoDto) {
        if (userInfoDto == null) {
            return null;
        }

        final Long authId = userInfoDto.getAuthId();
        final UserInfoEntity userInfoEntity = new UserInfoEntity();
        userInfoEntity.setAuthId(authId);
        userInfoEntity.setFirstName(userInfoDto.getFirstName());
        userInfoEntity.setLastName(userInfoDto.getLastName());
        userInfoEntity.setRegistrationTime(userInfoDto.getRegistrationTime());
        userInfoEntity.setEmail(userInfoDto.getEmail());
        userInfoEntity.setPhone(userInfoDto.getPhone());

        return userInfoEntity;
    }
}
