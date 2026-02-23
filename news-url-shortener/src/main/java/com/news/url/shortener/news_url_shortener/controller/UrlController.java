package com.news.url.shortener.news_url_shortener.controller;

import com.news.url.shortener.news_url_shortener.dto.UrlRequestDto;
import com.news.url.shortener.news_url_shortener.entity.UrlMapping;
import com.news.url.shortener.news_url_shortener.service.UrlService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    private UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<UrlMapping>  shorten(@Valid @RequestBody UrlRequestDto urlRequestDto) {
        return new ResponseEntity<>(urlService.createShortUrl(urlRequestDto.getLongUrl()), HttpStatus.CREATED);
    }

    @GetMapping("/{code}")
    public String redirect(@Valid @PathVariable String code) {
        return  urlService.redirect(code) ;
    }





}