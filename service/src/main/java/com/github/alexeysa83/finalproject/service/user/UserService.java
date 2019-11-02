package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

public interface UserService {

//   User create/delete service is made in transaction with AuthUserDAO methods

    UserInfoDto getById (long id);

    boolean updateUserInfo(UserInfoDto user);

    UserInfoDto addBadgeToUser (long authId, long badgeId);

    UserInfoDto deleteBadgeFromUser (long authId, long badgeId);
}
