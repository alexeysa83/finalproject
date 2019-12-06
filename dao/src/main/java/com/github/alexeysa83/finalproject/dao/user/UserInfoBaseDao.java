package com.github.alexeysa83.finalproject.dao.user;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;

/**
 * Create and delete user logic in AuthUserDAO methods in transactions
 */

public interface UserInfoBaseDao extends BaseDao<UserInfoDto> {

   UserInfoDto addBadgeToUser (Long authId, Long badgeId);

   UserInfoDto deleteBadgeFromUser (Long authId, Long badgeId);
   }
