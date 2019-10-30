package com.github.alexeysa83.finalproject.dao.entity;

import com.github.alexeysa83.finalproject.model.Role;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "auth_user")
public class AuthUserEntity {

    private long id;
    private String login;
    private String password;
    private Role role;
    private boolean isBlocked;

    private UserEntity user;

    private Set<NewsEntity> news;

    private Set<CommentEntity> messages;

    public AuthUserEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column (unique = true, nullable = false, length = 64)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column (nullable = false, length = 64)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column (nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(name = "is_blocked", nullable = false)
    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    @OneToOne(mappedBy = "authUser", cascade = CascadeType.ALL , orphanRemoval = true)
    public UserEntity getUser() {
        return user;
    }

//    cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "authUser")
    public Set<NewsEntity> getNews() {
        return news;
    }

    public void setNews(Set<NewsEntity> news) {
        this.news = news;
    }

    @OneToMany(mappedBy = "authUser")
    public Set<CommentEntity> getMessages() {
        return messages;
    }

    public void setMessages(Set<CommentEntity> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "AuthUserEntity{" +
                "id=" + id +
                ", login=" + login +
                ", role=" + role +
                ", isBlocked=" + isBlocked + '}';
    }
}
