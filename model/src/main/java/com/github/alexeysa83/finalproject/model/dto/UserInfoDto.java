package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class UserInfoDto {

    private Long authId;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
    private String userLogin;
    private List<BadgeDto> badges;

    public UserInfoDto() {
    }

    public UserInfoDto(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public UserInfoDto(Long authId, String firstName, String lastName, String email, String phone) {
        this.authId = authId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserInfoDto(Long authId, String firstName, String lastName, Timestamp registrationTime, String email, String phone, String userLogin) {
        this.authId = authId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationTime = registrationTime;
        this.email = email;
        this.phone = phone;
        this.userLogin = userLogin;
    }

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public List<BadgeDto> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeDto> badges) {
        this.badges = badges;
    }

    @Override
    public String toString() {
        return "User{" +
                "authId=" + authId +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", registrationTime=" + registrationTime +
                ", email=" + email +
                ", phone=" + phone +
                ", userLogin=" + userLogin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto user = (UserInfoDto) o;
        return Objects.equals(authId, user.authId) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                registrationTime.equals(user.registrationTime) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, firstName, lastName, registrationTime, email, phone);
    }
}
