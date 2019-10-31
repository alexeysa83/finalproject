package com.github.alexeysa83.finalproject.dao.entity;

import com.github.alexeysa83.finalproject.model.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "auth_user")
public class AuthUserEntity {

    private long id;
    private String login;
    private String password;
    private Role role;
    private boolean isDeleted;

    private UserInfoEntity user;

    private List<NewsEntity> news;

    private List<CommentEntity> comments;

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

    @Column(name = "is_deleted", nullable = false)
    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @OneToOne(mappedBy = "authUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL , orphanRemoval = true)
    public UserInfoEntity getUser() {
        return user;
    }

//    cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}

    public void setUser(UserInfoEntity user) {
        this.user = user;
    }

    @OneToMany(targetEntity =com.github.alexeysa83.finalproject.dao.entity.NewsEntity.class,
            mappedBy = "authUser",
            fetch = FetchType.LAZY)
    public List<NewsEntity> getNews() {
        return news;
    }

    public void setNews(List<NewsEntity> news) {
        this.news = news;
    }

    @OneToMany(targetEntity = com.github.alexeysa83.finalproject.dao.entity.CommentEntity.class,
            mappedBy = "authUser",
            fetch = FetchType.LAZY)
    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "AuthUserEntity{" +
                "id=" + id +
                ", login=" + login +
                ", role=" + role +
                ", isDeleted=" + isDeleted + '}';
    }
}
