package com.github.alexeysa83.finalproject.dao.repository;

import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<NewsEntity, Long> {

    @Modifying(clearAutomatically = true)
    @Query(value = "update NewsEntity n set n.title = :title, n.content = :content where n.id = :id")
    int updateContentTitleNews(@Param("id") Long id,
                               @Param("title") String title,
                               @Param("content") String content);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from NewsEntity n where n.id=:id")
    int deleteNews(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from CommentEntity c where c.news.id=:newsId")
    void deleteAllCommentOnNews(@Param("newsId") Long id);

    @Query(value = "from AuthUserEntity a where a.id=:id")
    AuthUserEntity getAuthUserById(@Param("id") Long id);

    /**
     * NEWS RATING
     */

    @Query(value = "select rating nr from news_rating nr where nr.auth_id=:authId and nr.news_id=:newsId",
            nativeQuery = true)
    Integer getRatingOnNewsFromUser(@Param("authId") Long authId, @Param("newsId") Long newsId);

    @Query(value = "select sum(rating) from news_rating nr where nr.news_id=:newsId", nativeQuery = true)
    Integer getTotalRatingOnNews(@Param("newsId") Long newsId);

    @Modifying(clearAutomatically = true)
    @Query(value = "insert into news_rating (auth_id, news_id, rating) values (:authId, :newsId, :rating)",
            nativeQuery = true)
    int addRatingOnNews(@Param("authId") Long authId, @Param("newsId") Long newsId, @Param("rating") int rating);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from news_rating nr where nr.auth_id=:authId and nr.news_id=:newsId",
            nativeQuery = true)
    int deleteSingleRatingOnNews(@Param("authId") Long authId, @Param("newsId") Long newsId);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from news_rating nr where nr.news_id=:newsId",
            nativeQuery = true)
    void deleteAllRatingOnNews(@Param("newsId") Long newsId);
}
