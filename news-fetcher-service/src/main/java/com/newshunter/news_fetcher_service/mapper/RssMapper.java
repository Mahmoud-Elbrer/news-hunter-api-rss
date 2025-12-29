package com.newshunter.news_fetcher_service.mapper;

import com.newshunter.news_fetcher_service.dto.RssItemDto;
import com.newshunter.news_fetcher_service.entity.RssItem;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class RssMapper {

    public static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static RssItem mapToEntity(RssItemDto categoryDto) {
        return mapper.map(categoryDto, RssItem.class);
    }

    public static RssItemDto mapToDto(RssItem category) {
        return mapper.map(category, RssItemDto.class);
    }
}
