package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;
import java.util.Objects;

public class CommentDto {

    private Long id;
    private String content;
    private Timestamp creationTime;
    private Long authId;
    private Long newsId;
    private String authorComment;
    private int ratingTotal;
    private Integer userInSessionRateOnThisComment;

    public CommentDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getAuthorComment() {
        return authorComment;
    }

    public void setAuthorComment(String authorComment) {
        this.authorComment = authorComment;
    }

    public int getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(int ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    public Integer getUserInSessionRateOnThisComment() {
        return userInSessionRateOnThisComment;
    }

    public void setUserInSessionRateOnThisComment(Integer userInSessionRateOnThisComment) {
        this.userInSessionRateOnThisComment = userInSessionRateOnThisComment;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto comment = (CommentDto) o;
        return id.equals(comment.id) &&
                content.equals(comment.content) &&
                creationTime.equals(comment.creationTime) &&
                authId.equals(comment.authId) &&
                newsId.equals(comment.newsId) &&
                authorComment.equals(comment.authorComment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, creationTime, authId, newsId, authorComment);
    }
}
