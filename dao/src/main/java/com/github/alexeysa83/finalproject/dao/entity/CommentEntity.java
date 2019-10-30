package com.github.alexeysa83.finalproject.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "comment")
public class CommentEntity {

    private long id;
    private String content;
    private Timestamp creationTime;
//    private long authId;
//    private long newsId;

    private AuthUserEntity authUser;

    private NewsEntity news;

    public CommentEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column (name = "creation_time")
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

//    @Column (name = "auth_id")
//    public long getAuthId() {
//        return authId;
//    }
//
//    public void setAuthId(long authId) {
//        this.authId = authId;
//    }
//
//    @Column (name = "news_id")
//    public long getNewsId() {
//        return newsId;
//    }
//
//    public void setNewsId(long newsId) {
//        this.newsId = newsId;
//    }

    @ManyToOne
    @JoinColumn (name = "auth_id")
    public AuthUserEntity getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUserEntity authUser) {
        this.authUser = authUser;
    }

    @ManyToOne
    @JoinColumn (name = "news_id")
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
//                ", authId=" + authId +
//                ", newsId=" + newsId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentEntity comm = (CommentEntity) o;
        return id == comm.id &&
                content.equals(comm.content) &&
                creationTime.equals(comm.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, creationTime);
    }
}
