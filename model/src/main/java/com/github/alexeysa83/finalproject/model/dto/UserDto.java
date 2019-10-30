package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class UserDto {

    private long authId;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
    private String userLogin;

    public UserDto() {
    }

    public UserDto(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
//        this.authId = authId;
    }

    public UserDto(long authId, String firstName, String lastName, String email, String phone) {
        this.authId = authId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public UserDto(long authId, String firstName, String lastName, Timestamp registrationTime, String email, String phone, String userLogin) {
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
        UserDto userDto = (UserDto) o;
        return authId == userDto.authId &&
                Objects.equals(firstName, userDto.firstName) &&
                Objects.equals(lastName, userDto.lastName) &&
                Objects.equals(registrationTime, userDto.registrationTime) &&
                Objects.equals(email, userDto.email) &&
                Objects.equals(phone, userDto.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authId, firstName, lastName, registrationTime, email, phone);
    }
}
