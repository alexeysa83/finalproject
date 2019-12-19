package com.github.alexeysa83.finalproject.web.Utils;

public abstract class MessageContainer {

    private MessageContainer() {
    }

    // String keys used in ModelMap
    public final static String ERROR_ATTRIBUTE = "error";
    public final static String MESSAGE_ATTRIBUTE = "message";
    public final static String USER_ATTRIBUTE = "user";
    public final static String PAGE_CURRENT = "currentPage";
    public final static String PAGES_TOTAL = "totalPages";
    public final static String NEWS_LIST = "newsList";
    public final static String NEWS_SINGLE = "news";
    public final static String COMMENT_LIST = "commentList";
    public final static String COMMENT_TO_UPDATE_ID = "commentToUpdateId";

    public final static String USER_BADGES_ATTRIBUTE = "userBadges";
        public final static String BADGES_ALL_FROM_DB = "badgesDB";
    public final static String BADGE_TO_UPDATE_ID = "badgeToUpdateId";

    //Errors
    public final static String INVALID_REPEAT_PASS = "invalid.repeatpass";
    public final static String WRONG_PASSWORD_ENTERED = "wrong.pass";
    public final static String LOGIN_IS_TAKEN = "login.istaken";
    public final static String NO_PERMISSION_TO_UPDATE = "no.permission";
    public final static String FAILED_TO_UPDATE = "update.fail";
    public final static String FAILED_TO_DELETE = "delete.fail";
    public final static String BADGE_NAME_IS_TAKEN = "badge.name.istaken";
    public final static String ERROR_UNKNOWN = "error.unknown";


    //Messages
    public final static String SUCCESSFUL_REGISTRATION = "registration.success";
    public final static String SUCCESSFUL_UPDATE = "update.success";
    public final static String SUCCESSFUL_DELETE = "delete.success";
    public final static String USER_DELETED = "deleted";
    public final static String NEWS_CREATED = "news.create.success";
    public final static String COMMENT_CREATED = "comment.create.success";

    public final static String BADGE_CREATED = "badge.create.success";



}
