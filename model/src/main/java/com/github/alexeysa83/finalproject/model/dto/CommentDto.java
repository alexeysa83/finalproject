package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;

public class CommentDto {

    private long id;
    private String content;
    private Timestamp creationTime;
    private long authId;
    private long newsId;
    private String authorComment;

    public CommentDto() {
    }

    public CommentDto(String content, Timestamp creationTime, long authId, long newsId, String authorComment) {
        this.content = content;
        this.creationTime = creationTime;
        this.authId = authId;
        this.newsId = newsId;
        this.authorComment = authorComment;
    }

    public CommentDto(long id, String content, Timestamp creationTime, long authId, long newsId, String authorComment) {
        this.id = id;
        this.content = content;
        this.creationTime = creationTime;
        this.authId = authId;
        this.newsId = newsId;
        this.authorComment = authorComment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public String getAuthorComment() {
        return authorComment;
    }

    public void setAuthorComment(String authorComment) {
        this.authorComment = authorComment;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content=" + content +
                ", creationTime=" + creationTime +
                ", authId=" + authId +
                ", newsId=" + newsId +
                ", authorComment=" + authorComment +
                '}';
    }
}
