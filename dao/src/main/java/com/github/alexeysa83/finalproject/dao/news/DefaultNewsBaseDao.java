package com.github.alexeysa83.finalproject.dao.news;

import com.github.alexeysa83.finalproject.dao.Utils.DaoAuthenticationUtils;
import com.github.alexeysa83.finalproject.dao.convert_entity.NewsConvert;
import com.github.alexeysa83.finalproject.dao.entity.AuthUserEntity;
import com.github.alexeysa83.finalproject.dao.entity.NewsEntity;
import com.github.alexeysa83.finalproject.dao.repository.NewsRepository;
import com.github.alexeysa83.finalproject.model.dto.NewsDto;
import com.github.alexeysa83.finalproject.model.dto.NewsRatingDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DefaultNewsBaseDao implements NewsBaseDao {

    private static final Logger log = LoggerFactory.getLogger(DefaultNewsBaseDao.class);

    private final NewsRepository newsRepository;

    public DefaultNewsBaseDao(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
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
    public NewsDto getById(Long newsId) {
        final Optional<NewsEntity> optional = newsRepository.findById(newsId);
        if (optional.isPresent()) {
            final NewsEntity newsEntity = optional.get();
            final NewsDto newsDto = NewsConvert.toDto(newsEntity);
            return addRatingFieldsToNewsDto(newsDto);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<NewsDto> getNewsOnPage(int page, int pageSize) {
        List<NewsDto> newsList = new ArrayList<>();

        final Page<NewsEntity> entityPage = newsRepository.findAll
                (PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "id"));

        entityPage.getContent().forEach(newsEntity -> {
            final NewsDto newsDto = NewsConvert.toDto(newsEntity);
            newsList.add(addRatingFieldsToNewsDto(newsDto));
        });
        return newsList;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    NewsDto addRatingFieldsToNewsDto(NewsDto newsDto) {
        final Long userInSessionAuthId = DaoAuthenticationUtils.getPrincipalUserAuthId();
        final Long newsId = newsDto.getId();
        final Integer rate = getRatingOnNewsFromUser(userInSessionAuthId, newsId);
        final int totalRatingOnNews = getTotalRatingOnNews(newsId);

        newsDto.setUserInSessionRateOnThisNews(rate);
        newsDto.setRatingTotal(totalRatingOnNews);
        return newsDto;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getRows() {
        final long count = newsRepository.count();
        return (int) count;
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
        newsRepository.deleteAllRatingOnNews(id);
        final int rowsDeleted = newsRepository.deleteNews(id);
        if (rowsDeleted > 0) {
            log.info("News id: {} deleted from DB at: {}", id, LocalDateTime.now());
        } else {
            log.error("Fail to delete news id from DB: {}, at: {}", id, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }

    /**
     * NEWS RATING
     */

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean addRatingOnNews(NewsRatingDto ratingDto) {
        final Long authId = ratingDto.getAuthId();
        final Long newsId = ratingDto.getNewsId();
        final int rowsUpdated = newsRepository.addRatingOnNews(authId, newsId, ratingDto.getRate());
        if (rowsUpdated > 0) {
            log.info("Rating on news id: {} from user id: {} added to DB at: {}", newsId, authId, LocalDateTime.now());
        } else {
            log.info("Failed to add rating: {} on news at: {}", ratingDto, LocalDateTime.now());
        }
        return rowsUpdated > 0;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public boolean deleteRatingFromNews(NewsRatingDto ratingDto) {
        final Long authId = ratingDto.getAuthId();
        final Long newsId = ratingDto.getNewsId();
        final int rowsDeleted = newsRepository.deleteSingleRatingOnNews(authId, newsId);
        if (rowsDeleted > 0) {
            log.info("Rating on news id: {} from user id: {} deleted from DB at: {}", newsId, authId, LocalDateTime.now());
        } else {
            log.info("Failed to delete rating: {} on news at: {}", ratingDto, LocalDateTime.now());
        }
        return rowsDeleted > 0;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getRatingOnNewsFromUser(Long authId, Long newsId) {
        Integer ratingOnNewsFromUser;
        try {
            ratingOnNewsFromUser = newsRepository.getRatingOnNewsFromUser(authId, newsId);
        } catch (NullPointerException e) {
            return null;
        }
        return ratingOnNewsFromUser;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public int getTotalRatingOnNews(Long newsId) {
        final Integer totalRatingOnNews = newsRepository.getTotalRatingOnNews(newsId);
        return (totalRatingOnNews == null) ? 0 : totalRatingOnNews;
    }
}