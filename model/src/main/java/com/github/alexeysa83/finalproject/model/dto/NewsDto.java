package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;

public class NewsDto {

    private long id;
    private String title;
    private String content;
    private Timestamp creationTime;
    private long authId;
    private String authorNews;

    public NewsDto() {
    }

    public NewsDto(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public NewsDto(String title, String content, Timestamp creationTime, long authId, String authorNews) {
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.authId = authId;
        this.authorNews = authorNews;
    }

    public NewsDto(long id, String title, String content, Timestamp creationTime, long authId, String authorNews) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.authId = authId;
        this.authorNews = authorNews;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long authId) {
        this.authId = authId;
    }

    public String getAuthorNews() {
        return authorNews;
    }

    public void setAuthorNews(String authorNews) {
        this.authorNews = authorNews;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title=" + title +
                ", creation time=" + creationTime +
                ", authId=" + authId +
                ", author=" + authorNews + '}';
    }
}
