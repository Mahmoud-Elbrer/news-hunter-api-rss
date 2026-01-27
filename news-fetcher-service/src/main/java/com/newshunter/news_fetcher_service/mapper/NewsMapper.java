package com.newshunter.news_fetcher_service.mapper;

import com.newshunter.news_fetcher_service.dto.NewsDto;
import com.newshunter.news_fetcher_service.entity.News;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class NewsMapper {

    public static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static News mapToEntity(NewsDto categoryDto) {
        return mapper.map(categoryDto, News.class);
    }

    public static NewsDto mapToDto(News category) {
        return mapper.map(category, NewsDto.class);
    }
}
