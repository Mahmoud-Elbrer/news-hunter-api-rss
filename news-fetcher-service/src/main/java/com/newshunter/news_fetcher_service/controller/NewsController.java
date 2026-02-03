package com.newshunter.news_fetcher_service.controller;

import com.newshunter.news_fetcher_service.dto.NewsDto;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.service.NewsCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
public class NewsController {


    private NewsCacheService newsCacheService ;

    public NewsController(NewsCacheService newsCacheService) {
        this.newsCacheService = newsCacheService;
    }

    @GetMapping
    public ResponseEntity<List<NewsDto>> getNewsFromCache() {
        List<NewsDto> newsDto =  newsCacheService.getNewsFromCache();

        return new ResponseEntity<>(newsDto , HttpStatus.OK) ;
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteNewsFromCache() {
        newsCacheService.deleteNewsFromCache();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
