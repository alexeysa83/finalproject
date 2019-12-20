package com.github.alexeysa83.finalproject.dao.repository;

import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import com.github.alexeysa83.finalproject.dao.entity.UserInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;

public interface UserInfoRepository extends JpaRepository <UserInfoEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query (value = "insert into user_info (auth_id, registration_time) values (:authId, :registrationTime)", nativeQuery = true)
    void saveUserInfo (@Param("authId")Long authId, @Param("registrationTime") Timestamp regTime);

    @Modifying(clearAutomatically = true)
    @Query (value = "update UserInfoEntity u set u.firstName=:firstName, u.lastName=:lastName, " +
            "u.email=:email, u.phone=:phone where u.authId=:authId" )
    int updateUserInfo (@Param("authId")Long authId,
                        @Param("firstName")String firstName,
                        @Param("lastName")String lastName,
                        @Param("email")String email,
                        @Param("phone")String phone);

    @Modifying (clearAutomatically = true)
    @Query (value = "delete from UserInfoEntity u where u.authId=:authId")
    int deleteUserInfo  (@Param("authId")Long id);

    @Query(value = "from BadgeEntity b where b.id=:id")
    BadgeEntity getBadgeById(@Param("id") Long id);

    @Query(value = "select sum(rating) from news_rating nr join news n on n.id = nr.news_id where n.auth_id=:authId",
            nativeQuery = true)
    Integer getRatingFromThisUserNews(@Param("authId") Long authId);

    @Query(value = "select sum(rating) from comment_rating cr join comment c on c.id = cr.comment_id where c.auth_id=:authId",
            nativeQuery = true)
    Integer getRatingFromThisUserComment(@Param("authId") Long authId);
}
