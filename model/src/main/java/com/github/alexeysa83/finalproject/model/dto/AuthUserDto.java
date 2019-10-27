package com.github.alexeysa83.finalproject.model.dto;

import com.github.alexeysa83.finalproject.model.Role;

public class AuthUserDto {

    private long id;
    private String login;
    private String password;
    private Role role;
    private boolean isBlocked;

    public AuthUserDto() {
    }

    // User created in login/registration servlets
    public AuthUserDto(String login, String password) {
        this.login = login;
        this.password = password;
        role = Role.USER;
        isBlocked = false;
    }

    public AuthUserDto(long id, String login, String password, Role role, boolean isBlocked) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "id=" + id +
                ", login=" + login +
                ", role=" + role +
                ", isBlocked=" + isBlocked + '}';
    }
}