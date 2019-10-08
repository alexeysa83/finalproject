package com.github.alexeysa83.finalproject.model;

import java.sql.Timestamp;

public class News {

    private long id;
    private String title;
    private String content;
    private Timestamp creationTime;
    private String authorNews;

    public News(long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
        creationTime = null;
        authorNews = null;
    }

    public News(String title, String content, Timestamp creationTime, String authorNews) {
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.authorNews = authorNews;
        id = 0;
    }

    public News(long id, String title, String content, Timestamp creationTime, String authorNews) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.creationTime = creationTime;
        this.authorNews = authorNews;
    }

    public long getId() {
        return id;
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

    public String getAuthorNews() {
        return authorNews;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title=" + title +
                ", creation time=" + creationTime +
                ", author=" + authorNews + '}';
    }
}
