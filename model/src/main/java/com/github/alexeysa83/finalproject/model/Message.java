package com.github.alexeysa83.finalproject.model;

import java.sql.Timestamp;

public class Message {

    private long id;
    private String content;
    private Timestamp creationTime;
    private long authId;
    private String authorMessage;

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
                ", authorMessage=" + authorMessage +
                '}';
    }
}