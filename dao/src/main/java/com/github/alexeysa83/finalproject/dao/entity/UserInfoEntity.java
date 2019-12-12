package com.github.alexeysa83.finalproject.dao.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_info")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInfoEntity {

    private Long authId;
    private String firstName;
    private String lastName;
    private Timestamp registrationTime;
    private String email;
    private String phone;

    private AuthUserEntity authUser;

    private List<BadgeEntity> badges = new ArrayList<>();

    public UserInfoEntity() {
    }

    @Id
    @Column(name = "auth_id", updatable = false)
//    @GenericGenerator(name = "gen",
//            strategy = "foreign",
//            parameters = @Parameter(name = "property", value = "authUser"))
//    @GeneratedValue(generator = "gen")

    public Long getAuthId() {
        return authId;
    }

    public void setAuthId(Long id) {
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

    @ManyToMany (mappedBy = "users", fetch = FetchType.LAZY)
    public List<BadgeEntity> getBadges() {
        return badges;
    }

    public void setBadges(List<BadgeEntity> badges) {
        this.badges = badges;
    }

    public void addBadge (BadgeEntity badgeEntity) {
        badges.add(badgeEntity);
        badgeEntity.getUsers().add(this);
    }

    public void deleteBadge (BadgeEntity badgeEntity) {
        badgeEntity.getUsers().remove(this);
        badges.remove(badgeEntity);
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
