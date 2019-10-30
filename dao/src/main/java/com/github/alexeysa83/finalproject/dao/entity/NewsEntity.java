package com.github.alexeysa83.finalproject.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "news")
public class NewsEntity {

    private long id;
    private String title;
    private String content;
    private Timestamp creationTime;
//    private long authId;

    private AuthUserEntity authUser;

    private Set<CommentEntity> messages;

    public NewsEntity() {
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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column
    @Lob
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

    @ManyToOne
    @JoinColumn (name = "auth_id")
    public AuthUserEntity getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUserEntity authUser) {
        this.authUser = authUser;
    }

    @OneToMany (mappedBy = "news")
    public Set<CommentEntity> getMessages() {
        return messages;
    }

    public void setMessages(Set<CommentEntity> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "NewsEntity{" +
                "id=" + id +
                ", title=" + title +
                ", creation time=" + creationTime +
//                ", authId=" + authId +
                '}';
    }

           @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsEntity news = (NewsEntity) o;
        return id == news.id &&
//                authId == news.authId &&
                title.equals(news.title) &&
                content.equals(news.content) &&
                creationTime.equals(news.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, creationTime);
    }
}
