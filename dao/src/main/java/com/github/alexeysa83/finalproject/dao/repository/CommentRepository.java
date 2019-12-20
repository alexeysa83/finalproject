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

    /**
     * COMMENT RATING
     */

    @Query(value = "select rating cr from comment_rating cr where cr.auth_id=:authId and cr.comment_id=:commentId",
            nativeQuery = true)
    Integer getRatingOnCommentFromUser(@Param("authId")Long authId, @Param("commentId")Long commentId);

    @Query(value = "select sum(rating) from comment_rating cr where cr.comment_id=:commentId",
            nativeQuery = true)
    Integer getTotalRatingOnComments(@Param("commentId")Long commentId);

    @Modifying (clearAutomatically = true)
    @Query (value = "insert into comment_rating (auth_id, comment_id, rating) values (:authId, :commentId, :rating)",
            nativeQuery = true)
    int addRatingOnComment(@Param("authId")Long authId, @Param("commentId")Long commentId, @Param("rating")int rating );

    @Modifying (clearAutomatically = true)
    @Query (value = "delete from comment_rating cr where cr.auth_id=:authId and cr.comment_id=:commentId",
            nativeQuery = true)
    int deleteSingleRatingOnComment(@Param("authId")Long authId, @Param("commentId")Long commentId);

    @Modifying (clearAutomatically = true)
    @Query (value = "delete from comment_rating cr where cr.comment_id=:commentId",
            nativeQuery = true)
    void deleteAllRatingOnComment(@Param("commentId")Long commentId);
}
