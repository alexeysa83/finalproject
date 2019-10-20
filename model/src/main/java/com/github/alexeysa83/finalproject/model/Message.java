package com.github.alexeysa83.finalproject.model;

import java.sql.Timestamp;

public class Message {

    private long id;
    private String content;
    private Timestamp creationTime;
    private long authId;
    private long newsId;
    private String authorMessage;

    public Message(String content, Timestamp creationTime, long authId, long newsId) {
        this.content = content;
        this.creationTime = creationTime;
        this.authId = authId;
        this.newsId = newsId;
    }

    public Message(long id, String content, Timestamp creationTime, long authId, long newsId, String authorMessage) {
        this.id = id;
        this.content = content;
        this.creationTime = creationTime;
        this.authId = authId;
        this.newsId = newsId;
        this.authorMessage = authorMessage;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public long getAuthId() {
        return authId;
    }

    public long getNewsId() {
        return newsId;
    }

    public String getAuthorMessage() {
        return authorMessage;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content=" + content +
                ", creationTime=" + creationTime +
                ", authId=" + authId +
                ", newsId=" + newsId +
                ", authorMessage=" + authorMessage +
                '}';
    }
}
