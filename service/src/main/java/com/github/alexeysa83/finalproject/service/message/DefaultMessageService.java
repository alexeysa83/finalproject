package com.github.alexeysa83.finalproject.service.message;

import com.github.alexeysa83.finalproject.dao.message.DefaultMessageBaseDao;
import com.github.alexeysa83.finalproject.dao.message.MessageBaseDao;
import com.github.alexeysa83.finalproject.model.Message;

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
    public Message createAndSave(Message message) {
        return messageDao.createAndSave(message);
    }

    @Override
    public List<Message> getMessagesOnNews(long newsId) {
        return messageDao.getMessagesOnNews(newsId);
    }

    @Override
    public boolean updateMessage(Message message) {
        return messageDao.update(message);
    }

    @Override
    public boolean deleteMessage(long id) {
        return messageDao.delete(id);
    }
}
