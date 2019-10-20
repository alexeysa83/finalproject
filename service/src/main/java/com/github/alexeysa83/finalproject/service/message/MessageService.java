package com.github.alexeysa83.finalproject.service.message;

import com.github.alexeysa83.finalproject.model.Message;

import java.util.List;

public interface MessageService {

    boolean createAndSave (Message message);

    List<Message> getMessagesOnNews (long newsId);
}
