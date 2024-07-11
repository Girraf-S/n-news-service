package com.solbeg.nnewsservice.mapper;

import com.solbeg.nnewsservice.entity.News;
import com.solbeg.nnewsservice.model.NewsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "time", ignore = true)
    News newsFromNewsRequest(NewsRequest newsRequest);
    @Mapping(target = "time", ignore = true)
    News updateNewsFromNewsRequest(NewsRequest newsRequest, @MappingTarget News news);


    NewsRequest newsRequestFromNews(News news);
}
