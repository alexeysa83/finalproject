package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;

public class UserDto {

    private long id;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
    private long authId;
    private String userLogin;

    public UserDto(Timestamp registrationTime, long authId) {
        this.registrationTime = registrationTime;
        this.authId = authId;
    }

    public UserDto(String firstName, String lastName, String email, String phone, long authId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.authId = authId;
    }

    public UserDto(long id, String firstName, String lastName, Timestamp registrationTime,
                   String email, String phone, long authId, String userLogin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registrationTime = registrationTime;
        this.email = email;
        this.phone = phone;
        this.authId = authId;
        this.userLogin = userLogin;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public long getAuthId() {
        return authId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", registrationTime=" + registrationTime + '\n' +
                ", email=" + email +
                ", phone=" + phone +
                ", authId=" + authId +
                ", userLogin=" + userLogin +
                '}';
    }
}
