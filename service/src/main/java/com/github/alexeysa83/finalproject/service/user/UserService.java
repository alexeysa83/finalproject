package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

public interface UserService {

//   User create/delete service is made in transaction with AuthUserService methods

    UserInfoDto getById (Long id);

    boolean updateUserInfo(UserInfoDto user);

    UserInfoDto addBadgeToUser (Long authId, Long badgeId);

    UserInfoDto deleteBadgeFromUser (Long authId, Long badgeId);
}
