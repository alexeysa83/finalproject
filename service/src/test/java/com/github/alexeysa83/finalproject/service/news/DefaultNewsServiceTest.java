package com.github.alexeysa83.finalproject.service.news;

import com.github.alexeysa83.finalproject.dao.news.NewsBaseDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultNewsServiceTest {

    @Mock
    NewsBaseDao newsDao;

    @InjectMocks
    DefaultNewsService newsService;

    @Test
    void getNewsTotalPages() {
        final int testRows = 101;
        final int testPages = 11;
        when(newsDao.getRows()).thenReturn(testRows);
        final int result = newsService.getNewsTotalPages();
        assertEquals(testPages,result);
    }
}