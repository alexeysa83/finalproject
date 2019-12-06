package com.github.alexeysa83.finalproject.dao.repository;

import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository <NewsEntity, Long> {

    @Modifying(clearAutomatically = true)
//    @Transactional
    @Query(value = "update NewsEntity n set n.title = :title, n.content = :content where n.id = :id")
    int updateContentTitle (@Param("id")Long id,
                            @Param("title")String title,
                            @Param("content") String content);




}
