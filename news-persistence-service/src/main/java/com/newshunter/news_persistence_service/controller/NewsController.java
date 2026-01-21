package com.newshunter.news_persistence_service.controller;

import com.newshunter.news_persistence_service.dto.NewsDto;
import com.newshunter.news_persistence_service.service.NewsService;
import com.newshunter.news_persistence_service.utils.AppConstraint;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/news")
public class NewsController {

    NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }


    @PostMapping
    public ResponseEntity<NewsDto> createNews(@Valid @RequestBody NewsDto newsDto) {

        return new ResponseEntity<>(newsService.createNews(newsDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NewsDto>> getNews() {

        return new ResponseEntity<>(newsService.getAllNews(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsDto> getNewsById(@PathVariable(name = "id") long id) {

        return new ResponseEntity<>(newsService.getNewsById(id), HttpStatus.OK);
    }


    @GetMapping("/paging")
    public ResponseEntity<List<NewsDto>> getPaginationNews(@RequestParam(name = "pageNo", defaultValue = AppConstraint.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = AppConstraint.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
        return new ResponseEntity<>(newsService.getPaginationNews(pageSize, pageNo), HttpStatus.OK);
    }


    @PutMapping("/{id}")
    public ResponseEntity<NewsDto> updateNews(@Valid @RequestBody NewsDto newsDto, @PathVariable(name = "id") long id) {
        return new ResponseEntity<>(newsService.updateNews(newsDto, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteNewsById(@PathVariable(name = "id") long id) {
        newsService.deleteNewsById(id);
    }

    @DeleteMapping
    public void deleteAllNews() {
        newsService.deleteAllNews();
    }


}