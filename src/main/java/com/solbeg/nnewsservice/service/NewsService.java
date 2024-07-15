package com.solbeg.nnewsservice.service;

import com.solbeg.nnewsservice.entity.News;
import com.solbeg.nnewsservice.mapper.NewsMapper;
import com.solbeg.nnewsservice.model.NewsRequest;
import com.solbeg.nnewsservice.model.NewsResponse;
import com.solbeg.nnewsservice.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Transactional
    public void createNews(NewsRequest newsRequest) {
        News news = newsMapper.newsFromNewsRequest(newsRequest, getUserIdFromSecurityContext());

        newsRepository.save(news);
    }

    @Transactional
    public void updateNews(NewsRequest newsRequest, Long id) {
        News news = getNewsByIdOrThrowException(id);

        validate(news);

        news  = newsMapper.update(news, newsRequest);

        newsRepository.save(news);
    }

    @Transactional(readOnly = true)
    public NewsResponse getNewsById(Long id) {
        News news = getNewsByIdOrThrowException(id);
        return newsMapper.toResponse(news);
    }

    @Transactional(readOnly = true)
    public Page<NewsResponse> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable).map(newsMapper::toResponse);
    }

    private News getNewsByIdOrThrowException(Long id) {
        return newsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("No news with id " + id)
        );
    }

    private static void validate(final News news) {
        final Long userIdFromSecurityContext = getUserIdFromSecurityContext();
        if (!Objects.equals(userIdFromSecurityContext, news.getUserId()))
            throw new AccessDeniedException("Only creator can update news");
    }

    private static Long getUserIdFromSecurityContext() {
        return Long.parseLong(
                AuthUtil.extractClaimStringValue(SecurityContextHolder.getContext().getAuthentication(), "id")
        );
    }
}
