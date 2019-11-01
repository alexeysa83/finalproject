package com.github.alexeysa83.finalproject.model.dto;

public class BadgeDto {

    private long id;
    private String badgeName;

    public BadgeDto() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
}
