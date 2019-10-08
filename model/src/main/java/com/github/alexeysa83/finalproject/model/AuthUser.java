package com.github.alexeysa83.finalproject.model;

public class AuthUser {

    private long id;
    private String login;
    private String password;
    private Role role;
    private boolean isBlocked;

    public AuthUser() {
    }

    // User created in login/registration servlets
    public AuthUser(String login, String password) {
        this.login = login;
        this.password = password;
        id = 0;
        role = null;
        isBlocked = false;
    }

    public AuthUser(long id, String login, String password, Role role, boolean isBlocked) {
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
