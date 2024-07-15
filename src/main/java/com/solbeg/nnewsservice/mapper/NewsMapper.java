package com.solbeg.nnewsservice.mapper;

import com.solbeg.nnewsservice.entity.News;
import com.solbeg.nnewsservice.model.NewsRequest;
import com.solbeg.nnewsservice.model.NewsResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(target = "time", ignore = true)
    News newsFromNewsRequest(NewsRequest newsRequest, Long userId);
    @Mapping(target = "time", ignore = true)
    News update(@MappingTarget News news, NewsRequest newsRequest);

    NewsResponse toResponse(News news);
}
