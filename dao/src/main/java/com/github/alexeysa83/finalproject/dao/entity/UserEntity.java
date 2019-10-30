package com.github.alexeysa83.finalproject.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
public class UserEntity {

    private long id;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;
//    private long authId;

    private AuthUserEntity authUser;

    public UserEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "first_name", length = 64, insertable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", length = 64, insertable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "registration_time", nullable = false, updatable = false)
    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    @Column (length = 64, insertable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column (length = 64, insertable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

//    @Column (name = "auth_id")
//    public long getAuthId() {
//        return authId;
//    }
//
//    public void setAuthId(long authId) {
//        this.authId = authId;
//    }

    @OneToOne
    @JoinColumn (name = "auth_id")
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
//                ", authId=" + authId +
                ", authUser=" + authUser +
                '}';
    }
}
