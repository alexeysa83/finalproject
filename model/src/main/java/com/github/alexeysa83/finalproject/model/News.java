package com.github.alexeysa83.finalproject.model;

import java.sql.Timestamp;

public class News {

    private long id;
    private String title;
    private String content;
    private Timestamp creationTime;
    private long authorId;
    private String authorLogin;

    public News(String title, String content, Timestamp creationTime, long authorId, String authorLogin) {
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.authorId = authorId;
        this.authorLogin = authorLogin;
        id = 0;
    }

    public News(long id, String title, String content, Timestamp creationTime, long authorId, String authorLogin) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.authorId = authorId;
        this.authorLogin = authorLogin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public long getAuthorId() {
        return authorId;
    }

    public String getAuthorLogin() {
        return authorLogin;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title=" + title +
                ", creation time=" + creationTime +
                ", author id=" + authorId;
    }
}
