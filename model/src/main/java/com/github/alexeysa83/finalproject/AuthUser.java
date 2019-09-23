package com.github.alexeysa83.finalproject;

public class AuthUser {

    private String login;
    private String password;
    private Role role;

    public AuthUser() {
    }

    public AuthUser(String username, String password, Role role) {
        this.login = username;
        this.password = password;
        this.role = role;
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
}
