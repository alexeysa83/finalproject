package com.github.alexeysa83.finalproject.dao.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "news")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NewsEntity {

    private Long id;
    private String title;
    private String content;
    private Timestamp creationTime;

    private AuthUserEntity authUser;

    private List<CommentEntity> comments = new ArrayList<>();

    public NewsEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column (nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column (nullable = false)
    @Lob
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column (name = "creation_time", nullable = false, updatable = false)
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "auth_id", nullable = false, updatable = false)
    public AuthUserEntity getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUserEntity authUser) {
        this.authUser = authUser;
    }

    @OneToMany (mappedBy = "news", fetch = FetchType.LAZY)
    public List<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "NewsEntity{" +
                "id=" + id +
                ", title=" + title +
                ", creation time=" + creationTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsEntity news = (NewsEntity) o;
        return id.equals(news.id) &&
                title.equals(news.title) &&
                content.equals(news.content) &&
                creationTime.equals(news.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, creationTime);
    }
}
