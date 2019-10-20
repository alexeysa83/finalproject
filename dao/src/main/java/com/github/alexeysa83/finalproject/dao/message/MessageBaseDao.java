package com.github.alexeysa83.finalproject.dao.message;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.Message;

import java.util.List;

public interface MessageBaseDao extends BaseDao <Message> {

    boolean createAndSave(Message message);

    List<Message> getMessagesOnNews (long newsId);

}
