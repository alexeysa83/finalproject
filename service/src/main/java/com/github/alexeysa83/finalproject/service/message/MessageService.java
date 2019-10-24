package com.github.alexeysa83.finalproject.service.message;

import com.github.alexeysa83.finalproject.model.Message;

import java.util.List;

public interface MessageService {

    Message createAndSave (Message message);

    List<Message> getMessagesOnNews (long newsId);

    boolean updateMessage (Message message);

    boolean deleteMessage (long id);
}
