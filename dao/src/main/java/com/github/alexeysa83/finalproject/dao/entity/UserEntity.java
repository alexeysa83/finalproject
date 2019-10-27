package com.github.alexeysa83.finalproject.dao.entity;

import java.sql.Timestamp;

public class UserEntity {

    private long id;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
    private long authId;

    private AuthUserEntity authUser;

    public UserEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
        this.authId = authId;
    }

    public AuthUserEntity getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUserEntity authUser) {
        this.authUser = authUser;
    }

           @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", registrationTime=" + registrationTime + '\n' +
                ", email=" + email +
                ", phone=" + phone +
                ", authId=" + authId +
                ", authUser=" + authUser +
                '}';
    }
}
