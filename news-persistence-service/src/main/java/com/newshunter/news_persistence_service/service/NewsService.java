package com.newshunter.news_persistence_service.service;

import com.newshunter.news_persistence_service.dto.NewsDto;

import java.util.List;

public interface NewsService {
    NewsDto createNews(NewsDto newsDto);

    List<NewsDto> getAllNews();

    NewsDto getNewsById(long id);

    List<NewsDto> getPaginationNews(int pageSize, int pageNo);

    NewsDto updateNews(NewsDto newsDto, long id);

    List<NewsDto> getNewsByChannel(long channelId);

    List<NewsDto> searchPostNews(String searchName);

    void deleteNewsById(long id);

    // todo : not recommended
    void deleteAllNews();

}
