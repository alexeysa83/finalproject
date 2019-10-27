package com.github.alexeysa83.finalproject.service.message;

import com.github.alexeysa83.finalproject.model.dto.MessageDto;

import java.util.List;

public interface MessageService {

    MessageDto createAndSave (MessageDto message);

    List<MessageDto> getMessagesOnNews (long newsId);

    boolean updateMessage (MessageDto message);

    boolean deleteMessage (long id);
}
