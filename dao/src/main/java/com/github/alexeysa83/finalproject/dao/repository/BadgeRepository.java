package com.github.alexeysa83.finalproject.dao.repository;

import com.github.alexeysa83.finalproject.dao.entity.BadgeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BadgeRepository extends JpaRepository <BadgeEntity, Long> {

    BadgeEntity findByBadgeName (String badgeName);

    @Modifying (clearAutomatically = true)
//    @Transactional
    @Query (value = "update BadgeEntity b set b.badgeName=:badgeName where b.id=:id")
    int updateBadgeName (@Param("id") Long id, @Param("badgeName") String badgeName);

    @Modifying (clearAutomatically = true)
//    @Transactional
    @Query (value = "delete from BadgeEntity b where b.id=:id")
    int deleteBadge  (@Param("id")Long id);
}
