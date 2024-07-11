package com.solbeg.nnewsservice.controller;

import com.solbeg.nnewsservice.entity.News;
import com.solbeg.nnewsservice.model.NewsRequest;
import com.solbeg.nnewsservice.service.NewsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('news:read')")
    public News getNewsById(@PathVariable Long id) {
        return newsService.readNews(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('news:write')")
    public void createNews(@RequestBody NewsRequest newsRequest,
                           HttpServletRequest request) {
        newsService.createNews(newsRequest, request);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('news:write')")
    public void updateNews(@PathVariable Long id,
                           @RequestBody NewsRequest newsRequest,
                           HttpServletRequest request) {
        newsService.updateNews(newsRequest,request, id);
    }
    
    @GetMapping
    @PreAuthorize("hasAuthority('news:read')")
    public Page<News> readAll(
            @RequestParam(value = "offset", defaultValue = "0") @Min(0) Integer offset,
            @RequestParam(value = "limit", defaultValue = "5") @Min(1) @Max(10)Integer limit
    ){
        return newsService.readAll(offset, limit);
    }

}
