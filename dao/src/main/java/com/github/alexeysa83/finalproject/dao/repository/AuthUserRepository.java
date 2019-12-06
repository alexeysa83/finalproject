package com.github.alexeysa83.finalproject.dao.repository;

import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthUserRepository extends JpaRepository <AuthUserEntity, Long> {

    AuthUserEntity findByLogin (String login);

    @Modifying (clearAutomatically = true)
//    @Transactional
    @Query (value = "update AuthUserEntity a set a.login=:login,a.password=:password, a.role=:role where a.id=:id")
    int updateLoginPasswordRole (@Param("id")Long id,
                                 @Param("login")String login,
                                 @Param("password") String password,
                                 @Param("role")Role role);

    @Modifying (clearAutomatically = true)
    //    @Transactional
    @Query (value = "update AuthUserEntity a set a.deleted=true where a.id=:id")
    int isDeletedSetTrue (@Param("id")Long id);
}
