package com.github.alexeysa83.finalproject.dao.convert;

import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

import java.util.HashSet;
import java.util.Set;

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

        Set<BadgeEntity> badgeEntities = userInfoEntity.getBadges();
        if (badgeEntities.size() > 0) {
            userInfoDto.setBadges(new HashSet<>());
            badgeEntities.forEach(badgeEntity -> {
                BadgeDto badgeDto = BadgeConvert.toDto(badgeEntity);
                userInfoDto.getBadges().add(badgeDto);
            });
        }
        return userInfoDto;
    }

    public static UserInfoEntity toEntity(UserInfoDto userInfoDto) {
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
}
