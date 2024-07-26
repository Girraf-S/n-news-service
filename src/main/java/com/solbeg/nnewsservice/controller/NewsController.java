package com.solbeg.nnewsservice.controller;

import com.solbeg.nnewsservice.model.NewsRequest;
import com.solbeg.nnewsservice.model.NewsResponse;
import com.solbeg.nnewsservice.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('news:read')")
    public NewsResponse getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('news:write')")
    public void createNews(@RequestBody NewsRequest newsRequest) {
        newsService.createNews(newsRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('news:write')")
    public void updateNews(@PathVariable Long id,
                           @RequestBody NewsRequest newsRequest){
        newsService.updateNews(newsRequest, id);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('news:read')")
    public Page<NewsResponse> getAllNews(Pageable pageable) {
        return newsService.getAllNews(pageable);
    }
}
