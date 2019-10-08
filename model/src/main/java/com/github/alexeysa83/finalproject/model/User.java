package com.github.alexeysa83.finalproject.model;

import java.sql.Timestamp;

public class User {

    private long id;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
    private String userLogin;

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

    public String getUserLogin() {
        return userLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName=" + firstName  +
                ", lastName=" + lastName  +
                ", registrationTime=" + registrationTime + '\n' +
                ", email=" + email +
                ", phone=" + phone +
                ", userLogin=" + userLogin +
                '}';
    }
}
