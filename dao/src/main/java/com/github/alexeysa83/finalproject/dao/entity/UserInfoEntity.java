package com.github.alexeysa83.finalproject.dao.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_info")
public class UserInfoEntity {

    private long authId;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;

    private AuthUserEntity authUser;

    private Set<BadgeEntity> badges = new HashSet<>();

    public UserInfoEntity() {
    }

    @Id
    @Column(name = "auth_id", updatable = false)
    @GenericGenerator(name = "gen",
            strategy = "foreign",
            parameters = @Parameter(name = "property", value = "authUser"))
    @GeneratedValue(generator = "gen")
    public long getAuthId() {
        return authId;
    }

    public void setAuthId(long id) {
        this.authId = id;
    }

    @Column(name = "first_name", length = 64, insertable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "last_name", length = 64, insertable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Column(name = "registration_time", nullable = false, updatable = false)
    public Timestamp getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Timestamp registrationTime) {
        this.registrationTime = registrationTime;
    }

    @Column(length = 64, insertable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(length = 64, insertable = false)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @PrimaryKeyJoinColumn(name = "auth_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    public AuthUserEntity getAuthUser() {
        return authUser;
    }

    public void setAuthUser(AuthUserEntity authUser) {
        this.authUser = authUser;
    }

    @ManyToMany (fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_badge",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "badge_id")})
    public Set<BadgeEntity> getBadges() {
        return badges;
    }

    public void setBadges(Set<BadgeEntity> badges) {
        this.badges = badges;
    }

    public void addBadge (BadgeEntity badgeEntity) {
        badges.add(badgeEntity);
        badgeEntity.getUsers().add(this);
    }

    public void deleteBadge (BadgeEntity badgeEntity) {
        badges.remove(badgeEntity);
        badgeEntity.getUsers().remove(this);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "authId=" + authId +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", registrationTime=" + registrationTime + '\n' +
                ", email=" + email +
                ", phone=" + phone +
                '}';
    }
}
