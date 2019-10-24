package com.github.alexeysa83.finalproject.dao.message;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.Message;

import java.util.List;

public interface MessageBaseDao extends BaseDao <Message> {

    Message createAndSave(Message message);

    List<Message> getMessagesOnNews (long newsId);

    boolean delete (long id);
}