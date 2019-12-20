package com.github.alexeysa83.finalproject.model.dto;

public class NewsRatingDto {

    private Long authId;
    private Long newsId;
    private Integer rate;

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

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "NewsRatingDto{" +
                "authId=" + authId +
                ", newsId=" + newsId +
                ", rate=" + rate +
                '}';
    }
}
