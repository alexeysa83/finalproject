package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.SessionManager;
import com.github.alexeysa83.finalproject.dao.convert_entity.NewsConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.repository.NewsRepository;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DefaultNewsBaseDao extends SessionManager implements NewsBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultNewsBaseDao.class);

    private final NewsRepository newsRepository;

    public DefaultNewsBaseDao(NewsRepository newsRepository, SessionFactory factory) {
        super(factory);
        this.newsRepository = newsRepository;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public NewsDto add(NewsDto newsDto) {
        final NewsEntity newsEntity = NewsConvert.toEntity(newsDto);
        try  {
            final Session session = getSession();
//            session.beginTransaction();
            final AuthUserEntity authUserEntity = session.get(AuthUserEntity.class, newsDto.getAuthId());
            newsEntity.setAuthUser(authUserEntity);
            session.save(newsEntity);
//            session.getTransaction().commit();
            log.info("News id: {} saved to DB at: {}", newsEntity.getId(), LocalDateTime.now());
            return NewsConvert.toDto(newsEntity);
        } catch (PersistenceException | NullPointerException e) {
            log.error("Fail to save new news to DB: {}, at: {}", newsDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public NewsDto getById(Long id) {
        final Optional<NewsEntity> optional = newsRepository.findById(id);
        if (optional.isPresent()) {
            final NewsEntity newsEntity = optional.get();
            return NewsConvert.toDto(newsEntity);
        }
        return null;
    }
    /**
     * Cast to int ok?
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getRows() {
        final long count = newsRepository.count();
        return (int) count;
    }

    @Override
    public List<NewsDto> getNewsOnPage(int page, int pageSize) {
        return null;
    }

    //    @Override
//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public List<NewsDto> getNewsOnPage(int page, int pageSize) {
//        List<NewsDto> newsList = new ArrayList<>();
//
//        try  {
//
//            Session session = getSession();
//            Query query = session.createQuery("from NewsEntity as n order by n.id desc ")
//                    .setMaxResults(pageSize)
//                    .setFirstResult((page - 1) * pageSize)
//                    .setReadOnly(true);
//            List<NewsEntity> list = query.list();
//            list.forEach(newsEntity -> {
//                NewsDto newsDto = NewsConvert.toDto(newsEntity);
//                newsList.add(newsDto);
//            });
//            return newsList;
//        } catch (PersistenceException e) {
//            log.error("Fail to get list of news from DB at: {}", LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }

    @Override
    public boolean update(NewsDto newsDto) {
        try (Session session = getSession()) {
            session.beginTransaction();
            final int i = session.createQuery
                    ("update NewsEntity n set n.title = :title, n.content = :content where n.id = :id")
                    .setParameter("title", newsDto.getTitle())
                    .setParameter("content", newsDto.getContent())
                    .setParameter("id", newsDto.getId())
                    .executeUpdate();
            session.getTransaction().commit();
            log.info("News id: {} updated in DB at: {}", newsDto.getId(), LocalDateTime.now());
            return i > 0;
        } catch (PersistenceException e) {
            log.error("Fail to update news in DB: {}, at: {}", newsDto, LocalDateTime.now(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        final Session session = getSession();
        session.clear();
        newsRepository.deleteById(id);
        return true;
    }

    //     @Override
//     @Transactional(propagation = Propagation.MANDATORY)
//    public boolean delete(Long id) {
//        try  {
//            Session session = getSession();
////                    session.createQuery("delete CommentEntity c where c.news.id=:id")
////                    .setParameter("id", id)
////                    .executeUpdate();
//            final int i = session.createQuery("delete NewsEntity n where n.id=:id")
//                    .setParameter("id", id)
//                    .executeUpdate();
////            session.clear();
////            final NewsEntity newsEntity = session.get(NewsEntity.class, id);
////            session.delete(newsEntity);
//            log.info("News id: {} deleted from DB at: {}", id, LocalDateTime.now());
//            return i > 0;
//        } catch (PersistenceException e) {
//            log.error("Fail to delete news id from DB: {}, at: {}", id, LocalDateTime.now(), e);
//            throw new RuntimeException(e);
//        }
//    }
}