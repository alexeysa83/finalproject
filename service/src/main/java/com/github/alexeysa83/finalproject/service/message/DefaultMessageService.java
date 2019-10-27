package com.github.alexeysa83.finalproject.service.message;

import com.github.alexeysa83.finalproject.dao.message.DefaultMessageBaseDao;
import com.github.alexeysa83.finalproject.dao.message.MessageBaseDao;
import com.github.alexeysa83.finalproject.model.dto.MessageDto;

import java.util.List;

public class DefaultMessageService implements MessageService {

    private MessageBaseDao messageDao = DefaultMessageBaseDao.getInstance();

    private static volatile MessageService instance;

    public static MessageService getInstance() {
        MessageService localInstance = instance;
        if (localInstance == null) {
            synchronized (MessageService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultMessageService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public MessageDto createAndSave(MessageDto message) {
        return messageDao.createAndSave(message);
    }

    @Override
    public List<MessageDto> getMessagesOnNews(long newsId) {
        return messageDao.getMessagesOnNews(newsId);
    }

    @Override
    public boolean updateMessage(MessageDto message) {
        return messageDao.update(message);
    }

    @Override
    public boolean deleteMessage(long id) {
        return messageDao.delete(id);
    }
}
