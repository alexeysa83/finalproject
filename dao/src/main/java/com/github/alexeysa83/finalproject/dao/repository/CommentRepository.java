package com.github.alexeysa83.finalproject.dao.repository;

import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.CommentEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "update CommentEntity n set n.content = :content where n.id = :id")
    int updateContentComment(@Param("id") Long id,
                             @Param("content") String content);

    @Modifying (clearAutomatically = true)
    @Query (value = "delete from CommentEntity c where c.id=:id")
    int deleteComment  (@Param("id")Long id);

    @Query(value = "from AuthUserEntity a where a.id=:id")
    AuthUserEntity getAuthUserById(@Param("id") Long id);

    @Query(value = "from NewsEntity n where n.id=:id")
    NewsEntity getNewsById(@Param("id") Long id);

}
