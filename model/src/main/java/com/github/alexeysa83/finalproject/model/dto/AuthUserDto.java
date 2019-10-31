package com.github.alexeysa83.finalproject.model.dto;

import com.github.alexeysa83.finalproject.model.Role;

public class AuthUserDto {

    private long id;
    private String login;
    private String password;
    private Role role;
    private boolean isBlocked;

    private UserInfoDto userInfoDto;

    public AuthUserDto() {
    }

    // User created in login/registration servlets
    public AuthUserDto(String login, String password, UserInfoDto userInfoDto) {
        this.login = login;
        this.password = password;
        role = Role.USER;
        isBlocked = false;
        this.userInfoDto = userInfoDto;
    }

    public AuthUserDto(long id, String login, String password, Role role, boolean isBlocked, UserInfoDto userInfoDto) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.isBlocked = isBlocked;
        this.userInfoDto = userInfoDto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public UserInfoDto getUserInfoDto() {
        return userInfoDto;
    }

    public void setUserInfoDto(UserInfoDto userInfoDto) {
        this.userInfoDto = userInfoDto;
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
