package com.github.alexeysa83.finalproject.service.user;

import com.github.alexeysa83.finalproject.dao.user.UserInfoBaseDao;
import com.github.alexeysa83.finalproject.model.dto.UserInfoDto;
import org.springframework.transaction.annotation.Transactional;

public class DefaultUserService implements UserService {

    // Colours for user rating
    private final String GREEN_COLOUR = "#28fc34";
    private final String RED_COLOUR = "red";
    private final String BLACK_COLOUR = "black";

    private UserInfoBaseDao userDAO;

    public DefaultUserService(UserInfoBaseDao userDAO) {
        this.userDAO = userDAO;
    }

    //   User create/delete service is made in transaction with AuthUserService methods

    @Override
    @Transactional
    public UserInfoDto getById(Long id) {
        final UserInfoDto userInfoDto = userDAO.getById(id);
        setColourToRating(userInfoDto);
        return userInfoDto;
    }

    @Override
    @Transactional
    public boolean updateUserInfo(UserInfoDto user) {
        return userDAO.update(user);
    }

    @Override
    @Transactional
    public UserInfoDto addBadgeToUser(Long authId, Long badgeId) {
        return userDAO.addBadgeToUser(authId, badgeId);
    }

    @Override
    @Transactional
    public UserInfoDto deleteBadgeFromUser(Long authId, Long badgeId) {
        return userDAO.deleteBadgeFromUser(authId, badgeId);
    }

    private void setColourToRating(UserInfoDto userInfoDto) {
        final int userRating = userInfoDto.getUserRating();
        if (userRating > 0) {
            userInfoDto.setRatingColour(GREEN_COLOUR);
        } else if (userRating < 0) {
            userInfoDto.setRatingColour(RED_COLOUR);
        } else {
            userInfoDto.setRatingColour(BLACK_COLOUR);
        }
    }
}
