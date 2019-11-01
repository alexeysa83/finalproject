package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

public class UserInfoDto {

    private long authId;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
    private String userLogin;
    private Set <BadgeDto> badges;

    public UserInfoDto() {
    }

    public UserInfoDto(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
//        this.authId = authId;
    }

    public UserInfoDto(long authId, String firstName, String lastName, String email, String phone) {
        this.authId = authId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserInfoDto(long authId, String firstName, String lastName, Timestamp registrationTime, String email, String phone, String userLogin) {
        this.authId = authId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationTime = registrationTime;
        this.email = email;
        this.phone = phone;
        this.userLogin = userLogin;
    }

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
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

    public Set<BadgeDto> getBadges() {
        return badges;
    }

    public void setBadges(Set<BadgeDto> badges) {
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

    // UserLogin filed is not included as it references to AuthUser
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDto userInfoDto = (UserInfoDto) o;
        return authId == userInfoDto.authId &&
                Objects.equals(firstName, userInfoDto.firstName) &&
                Objects.equals(lastName, userInfoDto.lastName) &&
                Objects.equals(registrationTime, userInfoDto.registrationTime) &&
                Objects.equals(email, userInfoDto.email) &&
                Objects.equals(phone, userInfoDto.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, firstName, lastName, registrationTime, email, phone);
    }
}
