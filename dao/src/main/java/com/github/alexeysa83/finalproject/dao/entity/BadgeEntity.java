package com.github.alexeysa83.finalproject.dao.entity;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "badge")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BadgeEntity {

    private long id;
    private String badgeName;

    private Set<UserInfoEntity>users = new HashSet<>();

    public BadgeEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
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
//    @ManyToMany (mappedBy = "badges", fetch = FetchType.LAZY)
    public Set<UserInfoEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserInfoEntity> users) {
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
        return id == badge.id &&
                badgeName.equals(badge.badgeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, badgeName);
    }
}
