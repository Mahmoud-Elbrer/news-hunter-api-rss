package com.newshunter.news_persistence_service.mapper;

import com.newshunter.news_persistence_service.dto.NewsDto;
import com.newshunter.news_persistence_service.entity.News;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class NewsMapper {

    public static ModelMapper mapper;

    static {
        mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static News mapToNews(NewsDto newsDto) {
        return mapper.map(newsDto, News.class);
    }

    public static NewsDto mapToNewsDto(News news) {
        return mapper.map(news, NewsDto.class);
    }

     public static com.newshunter.news_persistence_service.entity.News mapToPersistenceNews(com.newshunter.news_fetcher_service.entity.News fetcherNews) {
        return mapper.map(fetcherNews, com.newshunter.news_persistence_service.entity.News.class);
    }


}
