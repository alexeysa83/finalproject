package com.github.alexeysa83.finalproject.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class CommentEntity {

    private Long id;
    private String content;
    private Timestamp creationTime;

    private AuthUserEntity authUser;

    private NewsEntity news;

    public CommentEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column (nullable = false, length = 2012)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column (name = "creation_time", nullable = false, updatable = false)
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "auth_id", nullable = false, updatable = false)
    public AuthUserEntity getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUserEntity authUser) {
        this.authUser = authUser;
    }

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "news_id", nullable = false, updatable = false)
    public NewsEntity getNews() {
        return news;
    }

    public void setNews(NewsEntity news) {
        this.news = news;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content=" + content +
                ", creationTime=" + creationTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity comment = (CommentEntity) o;
        return id.equals(comment.id) &&
                content.equals(comment.content) &&
                creationTime.equals(comment.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, creationTime);
    }
}
