package com.github.alexeysa83.finalproject.dao.convert;

import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

public abstract class BadgeConvert {

    private BadgeConvert() {}

    public static BadgeDto toDto(BadgeEntity badgeEntity) {
        if (badgeEntity == null) {
            return null;
        }
        final BadgeDto badgeDto = new BadgeDto();
        badgeDto.setId(badgeEntity.getId());
        badgeDto.setBadgeName(badgeEntity.getBadgeName());
        return badgeDto;
    }

    public static BadgeEntity toEntity(BadgeDto badgeDto) {
        if (badgeDto == null) {
            return null;
        }
        final BadgeEntity badgeEntity = new BadgeEntity();
        badgeEntity.setId(badgeDto.getId());
        badgeEntity.setBadgeName(badgeDto.getBadgeName());
        return badgeEntity;
    }
    }
