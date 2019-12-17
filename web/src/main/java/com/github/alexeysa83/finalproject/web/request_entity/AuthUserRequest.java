package com.github.alexeysa83.finalproject.web.request_entity;

import javax.validation.constraints.Pattern;

public class AuthUserRequest {

    @Pattern(regexp = "^[\\wА-ЯЁа-я-]{2,15}$", message = "invalid.login")
    private String login;
    @Pattern(regexp = "^[\\wА-ЯЁа-я-]{2,15}$", message = "invalid.pass")
    private String password;
    @Pattern(regexp = "ADMIN|USER")
    private String role;

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
