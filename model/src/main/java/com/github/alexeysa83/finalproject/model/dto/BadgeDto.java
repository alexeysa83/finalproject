package com.github.alexeysa83.finalproject.model.dto;

import java.util.Objects;

public class BadgeDto {

    private Long id;
    private String badgeName;

    public BadgeDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    @Override
    public String toString() {
        return "BadgeDto{" +
                "id=" + id +
                ", badgeName='" + badgeName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeDto badgeDto = (BadgeDto) o;
        return id.equals(badgeDto.id) &&
                badgeName.equals(badgeDto.badgeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, badgeName);
    }
}
