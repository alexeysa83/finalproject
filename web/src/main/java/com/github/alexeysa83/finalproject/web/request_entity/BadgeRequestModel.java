package com.github.alexeysa83.finalproject.web.request_entity;

import com.github.alexeysa83.finalproject.model.dto.BadgeDto;

import javax.validation.constraints.Pattern;

public class BadgeRequestModel {

    @Pattern(regexp = "^[A-ZА-ЯЁ]{2,15}$", message = "invalid.badge.name")
    private String badgeName;

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    public BadgeDto convertToBadgeDto() {
        final BadgeDto badgeDto = new BadgeDto();
        badgeDto.setBadgeName(badgeName);
        return badgeDto;
    }
}
