package com.github.alexeysa83.finalproject.dao.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "badge")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BadgeEntity {

    private Long id;
    private String badgeName;

    private List<UserInfoEntity> users = new ArrayList<>();

    public BadgeEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "badge_name", unique = true, nullable = false)
    public String getBadgeName() {
        return badgeName;
    }

    public void setBadgeName(String badgeName) {
        this.badgeName = badgeName;
    }

    @ManyToMany (fetch = FetchType.LAZY)
    @JoinTable(name = "user_badge",
            joinColumns = {@JoinColumn(name = "badge_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    public List<UserInfoEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfoEntity> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "BadgeEntity{" +
                "id=" + id +
                ", badgeName='" + badgeName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BadgeEntity badge = (BadgeEntity) o;
        return id.equals(badge.id) &&
                badgeName.equals(badge.badgeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, badgeName);
    }
}
