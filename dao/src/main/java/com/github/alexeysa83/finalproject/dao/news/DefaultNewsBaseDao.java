package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.ConvertEntityDTO;
import com.github.alexeysa83.finalproject.dao.DataSource;
import com.github.alexeysa83.finalproject.dao.HibernateUtil;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DefaultNewsBaseDao implements NewsBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultNewsBaseDao.class);
    private DataSource mysql = DataSource.getInstance();

    private static volatile NewsBaseDao instance;

    public static NewsBaseDao getInstance() {
        NewsBaseDao localInstance = instance;
        if (localInstance == null) {
            synchronized (NewsBaseDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultNewsBaseDao();
                }
            }
        }
        return localInstance;
    }


    @Override
    public NewsDto createAndSave(NewsDto newsDto) {
        final NewsEntity newsEntity = ConvertEntityDTO.NewsToEntity(newsDto);
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            final AuthUserEntity authUserEntity = session.get(AuthUserEntity.class, newsDto.getAuthId());
            newsEntity.setAuthUser(authUserEntity);
            session.save(newsEntity);
            session.getTransaction().commit();
            session.close();
        } catch (PersistenceException | NullPointerException e) {
            log.error("Fail to save new news to DB: {}, at: {}", newsDto, LocalDateTime.now(), e);
            return null;
        }
        log.info("News id: {} saved to DB at: {}", newsEntity.getId(), LocalDateTime.now());
        return ConvertEntityDTO.NewsToDto(newsEntity);
    }

    @Override
    public NewsDto getById(long id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        final NewsEntity newsEntity = session.get(NewsEntity.class, id);
        session.getTransaction().commit();
        session.close();
        return ConvertEntityDTO.NewsToDto(newsEntity);
    }

    // add page and limit parameters to get news on page method
    @Override
    public List<NewsDto> getNewsOnPage() {
        List<NewsDto> newsList = new ArrayList<>();

        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            Query query = session.createQuery("from NewsEntity as n order by n.id desc ")
                    .setMaxResults(10)
                    .setReadOnly(true);
            List <NewsEntity>list = query.list();
            list.forEach(newsEntity -> {
                NewsDto newsDto = ConvertEntityDTO.NewsToDto(newsEntity);
                newsList.add(newsDto);
            });
            return newsList;
        } catch (PersistenceException e) {
            log.error("Fail to get list of news from DB at: {}", LocalDateTime.now(), e);
           return null;
        }
    }

    @Override
    public boolean update(NewsDto newsDto) {
        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();

            NewsEntity newsEntityToUpdate = session.get(NewsEntity.class, newsDto.getId());
            newsEntityToUpdate.setTitle(newsDto.getTitle());
            newsEntityToUpdate.setContent(newsDto.getContent());
            session.save(newsEntityToUpdate);
            session.getTransaction().commit();
            session.close();
            log.info("News id: {} updated in DB at: {}", newsDto.getId(), LocalDateTime.now());
            return true;
        } catch (PersistenceException | NullPointerException e) {
            log.error("Fail to update news in DB: {}, at: {}", newsDto, LocalDateTime.now(), e);
            return false;
        }
    }

    @Override
    public boolean delete(long id) {

        try {
            Session session = HibernateUtil.getSession();
            session.beginTransaction();
            NewsEntity newsEntityToDelete = session.get(NewsEntity.class, id);
            session.delete(newsEntityToDelete);
            session.getTransaction().commit();
            session.close();
            log.info("News id: {} deleted from DB at: {}", id, LocalDateTime.now());
            return true;
        } catch (PersistenceException e) {
            log.error("Fail to delete news id from DB: {}, at: {}", id, LocalDateTime.now(), e);
            return false;
        }
    }
}