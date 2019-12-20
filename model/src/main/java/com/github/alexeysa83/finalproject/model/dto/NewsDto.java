package com.github.alexeysa83.finalproject.model.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewsDto {

    private Long id;
    private String title;
    private String content;
    private Timestamp creationTime;
    private Long authId;
    private String authorNews;
    private int ratingTotal;
    private String ratingColour;
    private Integer userInSessionRateOnThisNews;

    private List<CommentDto> comments = new ArrayList<>();

    public NewsDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public String getAuthorNews() {
        return authorNews;
    }

    public void setAuthorNews(String authorNews) {
        this.authorNews = authorNews;
    }

    public int getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(int ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    public String getRatingColour() {
        return ratingColour;
    }

    public void setRatingColour(String ratingColour) {
        this.ratingColour = ratingColour;
    }

    public Integer getUserInSessionRateOnThisNews() {
        return userInSessionRateOnThisNews;
    }

    public void setUserInSessionRateOnThisNews(Integer userInSessionRateOnThisNews) {
        this.userInSessionRateOnThisNews = userInSessionRateOnThisNews;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsDto newsDto = (NewsDto) o;
        return id.equals(newsDto.id) &&
                title.equals(newsDto.title) &&
                content.equals(newsDto.content) &&
                creationTime.equals(newsDto.creationTime) &&
                authId.equals(newsDto.authId) &&
                authorNews.equals(newsDto.authorNews);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, creationTime, authId, authorNews);
    }
}
