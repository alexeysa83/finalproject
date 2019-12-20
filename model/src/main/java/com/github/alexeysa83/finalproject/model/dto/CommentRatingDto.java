package com.github.alexeysa83.finalproject.model.dto;

public class CommentRatingDto {

    private Long authId;
    private Long commentId;
    private Integer rate;

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long authId) {
        this.authId = authId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "CommentRatingDto{" +
                "authId=" + authId +
                ", commentId=" + commentId +
                ", rate=" + rate +
                '}';
    }
}
