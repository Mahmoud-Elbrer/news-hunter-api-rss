package com.newshunter.news_persistence_service.service.impl;

import com.newshunter.news_persistence_service.dto.NewsDto;
import com.newshunter.news_persistence_service.entity.News;
import com.newshunter.news_persistence_service.exception.ResourceNotFoundException;
import com.newshunter.news_persistence_service.mapper.NewsMapper;
import com.newshunter.news_persistence_service.repository.NewsRepository;
import com.newshunter.news_persistence_service.service.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class NewsServiceImpl implements NewsService {


    //@Autowired
    private final NewsRepository newsRepository;

    public NewsServiceImpl(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @Override
    public NewsDto createNews(NewsDto newsDto) {

        News news = NewsMapper.mapToNews(newsDto);

        News savedNews = newsRepository.save(news);

        return NewsMapper.mapToNewsDto(savedNews);
    }

    @Override
    public List<NewsDto> getAllNews() {

        List<News> news = newsRepository.findAll();

        return news.stream().map(NewsMapper::mapToNewsDto).collect(Collectors.toList());
    }


    @Override
    public NewsDto getNewsById(long id) {

        News news = newsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("News", "id", id));
        ;

        return NewsMapper.mapToNewsDto(news);
    }


    @Override
    public List<NewsDto> getPaginationNews(int pageSize, int pageNo) {

        // to limit pageSize
        if (pageSize > 20) {pageSize = 20;}

        // create Pageable instance
        PageRequest pageable = PageRequest.of(pageNo, pageSize);

        // findAll by pageable
        Page<News> news = newsRepository.findAll(pageable);

        // Get content form page object
        List<News> pageContent = news.getContent();

        // Get the content of the page with Response
        List<NewsDto> newsContent = pageContent.stream().map(NewsMapper::mapToNewsDto).toList();

        return newsContent;
    }

    @Override
    public NewsDto updateNews(NewsDto newsDto, long id) {
        return null;
    }

    @Override
    public List<NewsDto> getNewsByChannel(long channelId) {
        return null;
    }

    @Override
    public List<NewsDto> searchPostNews(String searchName) {
        return null;
    }


    @Override
    public void deleteNewsById(long id) {
        //  get News by id form the database
        News news = newsRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("News", "id", id));

        newsRepository.delete(news);
    }


    // todo : not recommended
    @Override
    public void deleteAllNews() {
        newsRepository.deleteAll();
    }


}
