package com.newshunter.news_fetcher_service.mapper;

import com.newshunter.news_fetcher_service.dto.NewsItemDto;
import com.newshunter.news_fetcher_service.entity.NewsItem;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class NewsItemMapper {

    public static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static NewsItem mapToEntity(NewsItemDto categoryDto) {
        return mapper.map(categoryDto, NewsItem.class);
    }

    public static NewsItemDto mapToDto(NewsItem category) {
        return mapper.map(category, NewsItemDto.class);
    }
}
