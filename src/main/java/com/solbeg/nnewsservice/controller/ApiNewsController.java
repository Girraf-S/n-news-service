package com.solbeg.nnewsservice.controller;

import com.solbeg.nnewsservice.entity.News;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class ApiNewsController { //todo class to validate authorities
    List<News> news = new ArrayList<>();

    {
        news.add(News.builder()
                .id(1L)
                .text("""
                        Сегодня солнце зашло за тучи
                        Сегодня волны бьют так больно
                        Я видел как умирала надежда Ямайки
                        Моя душа плачет""")
                .title("Аргентина:Ямайка - 5:0")
                .time(LocalDateTime.now())
                .build());
        news.add(News.builder()
                .id(2L)
                .time(LocalDateTime.now())
                .title("Все идет по плану")
                .text("""
                        Границы ключ переломлен пополам
                        А наш батюшка Ленин совсем усоп
                        Он разложился на плесень и на липовый мёд
                        А перестройка всё идёт и идёт по плану
                        И вся грязь превратилась в голый лёд""")
                .build());
        news.add(News.builder()
                .id(3L)
                .time(LocalDateTime.now())
                .title("Прыгну со скалы")
                .text("""
                        С головы сорвал ветер мой колпак
                        Я хотел любви но вышло всё не так
                        Знаю я ничего в жизни не вернуть
                        И теперь у меня один лишь только путь""")
                .build());
        news.add(News.builder()
                .id(4L)
                .time(LocalDateTime.now())
                .title("Пачка сигарет")
                .text("""
                        Я сижу и смотрю в чужое небо из чужого окна
                        И не вижу ни одной знакомой звезды
                        Я ходил по всем дорогам и туда, и сюда
                        Обернулся и не смог разглядеть следы""")
                .build());

    }

    @GetMapping
    @PreAuthorize("hasAuthority('news:read')")
    public List<News> getAll() {
        return news;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('news:read')")
    public News getNewsById(@PathVariable Long id) {
        return news.stream().filter(map -> map.getId().equals(id)).findFirst().orElseThrow();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('news:write')")
    public boolean createNews(@RequestBody News news, Authentication authentication) {
        System.out.println(authentication.getAuthorities());
        System.out.println(authentication.getPrincipal());
        return this.news.add(news);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('news:write')")
    public News deleteNews(@PathVariable Long id) {
        return news.remove(id.intValue() - 1);
    }

}
