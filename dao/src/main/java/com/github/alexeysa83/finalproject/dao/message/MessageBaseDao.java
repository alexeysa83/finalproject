package com.github.alexeysa83.finalproject.dao.message;

import com.github.alexeysa83.finalproject.dao.BaseDao;
import com.github.alexeysa83.finalproject.model.dto.MessageDto;

import java.util.List;

public interface MessageBaseDao extends BaseDao <MessageDto> {

    MessageDto createAndSave(MessageDto messageDto);

    List<MessageDto> getMessagesOnNews (long newsId);

    boolean delete (long id);
}
