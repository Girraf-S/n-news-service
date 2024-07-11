package com.solbeg.nnewsservice.service;

import com.solbeg.nnewsservice.entity.News;
import com.solbeg.nnewsservice.mapper.NewsMapper;
import com.solbeg.nnewsservice.model.NewsRequest;
import com.solbeg.nnewsservice.repository.NewsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final JwtService jwtService;

    public void createNews(NewsRequest newsRequest, HttpServletRequest request) {
        Long userId = jwtService.extractId(
                jwtService.getJwtOrThrowException(
                        request.getHeader(HttpHeaders.AUTHORIZATION)
                ));
        News news = newsMapper.newsFromNewsRequest(newsRequest);
        news.setUserId(userId);
        newsRepository.save(news);
    }

    public void updateNews(NewsRequest newsRequest, HttpServletRequest request, Long id) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        System.out.println();
        System.out.println(header);
        System.out.println();
        Long userId = jwtService.extractId(
                jwtService.getJwtOrThrowException(
                        header
                ));
        News news = readNews(id);
        if (!Objects.equals(userId, news.getUserId()))
            throw new AccessDeniedException("Only creator can update news");

        newsRepository.save(newsMapper.updateNewsFromNewsRequest(newsRequest, news));
    }

    public News readNews(Long id) {
        return newsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No news with id " + id)
        );
    }

    public Page<News> readAll(int offset, int limit) {
        return newsRepository.findAll(PageRequest.of(offset, limit));
    }
}
