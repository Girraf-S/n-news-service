package com.solbeg.nnewsservice.config;

import com.solbeg.nnewsservice.mapper.NewsMapper;
import com.solbeg.nnewsservice.mapper.NewsMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public NewsMapper newsMapper(){
        return new NewsMapperImpl();
    }
}
