package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.convert_entity.NewsConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.repository.NewsRepository;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class DefaultNewsBaseDao implements NewsBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultNewsBaseDao.class);

    private final NewsRepository newsRepository;

    public DefaultNewsBaseDao(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     *
     * @param newsDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public NewsDto add(NewsDto newsDto) {
        final AuthUserEntity authUserEntity = newsRepository.getAuthUserById(newsDto.getAuthId());
        final NewsEntity newsEntity = NewsConvert.toEntity(newsDto);
        newsEntity.setAuthUser(authUserEntity);
        authUserEntity.getNews().add(newsEntity);

        final NewsEntity savedNews = newsRepository.save(newsEntity);
        if (savedNews != null) {
            log.info("News id: {} saved to DB at: {}", newsEntity.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to save new news to DB: {}, at: {}", newsDto, LocalDateTime.now());
        }
        return NewsConvert.toDto(savedNews);
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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getRows() {
        final long count = newsRepository.count();
        return (int) count;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<NewsDto> getNewsOnPage(int page, int pageSize) {
        List<NewsDto> newsList = new ArrayList<>();

        final Page<NewsEntity> entityPage = newsRepository.findAll
                (PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id"));

        entityPage.getContent().forEach(newsEntity -> {
            final NewsDto newsDto = NewsConvert.toDto(newsEntity);
            newsList.add(newsDto);
        });
        return newsList;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean update(NewsDto newsDto) {
        final int rowsUpdated = newsRepository.updateContentTitleNews(
                newsDto.getId(),
                newsDto.getTitle(),
                newsDto.getContent());

        if (rowsUpdated > 0) {
            log.info("News id: {} updated in DB at: {}", newsDto.getId(), LocalDateTime.now());
        } else {
            log.error("Fail to update news in DB: {}, at: {}", newsDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean delete(Long id) {
        newsRepository.deleteAllCommentOnNews(id);
        final int rowsDeleted = newsRepository.deleteNews(id);
        if (rowsDeleted > 0) {
            log.info("News id: {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete news id from DB: {}, at: {}", id, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }
}