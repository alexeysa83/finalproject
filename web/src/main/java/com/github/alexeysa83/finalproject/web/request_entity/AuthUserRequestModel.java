package com.github.alexeysa83.finalproject.web.request_entity;

import com.github.alexeysa83.finalproject.model.Role;
import com.github.alexeysa83.finalproject.model.dto.AuthUserDto;

import javax.validation.constraints.Pattern;

public class AuthUserRequestModel {

    @Pattern(regexp = "^[\\wА-ЯЁа-я-]{2,15}$", message = "invalid.login")
    private String login;
    @Pattern(regexp = "^[\\wА-ЯЁа-я-]{2,15}$", message = "invalid.pass")
    private String password;

    private String repeatPassword;

    @Pattern(regexp = "ADMIN|USER", message = "invalid.role")
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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getRole() {
        return role;
    }

    public Role getRoleInEnum() {
        return Role.valueOf(role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isPasswordMatchRepeatPassword () {
        return password.equals(repeatPassword);
    }

    public AuthUserDto convertToAuthUserDto () {
        return new AuthUserDto(login, password);
    }
}
