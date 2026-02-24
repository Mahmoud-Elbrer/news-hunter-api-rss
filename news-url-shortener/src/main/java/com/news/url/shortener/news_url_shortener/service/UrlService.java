package com.news.url.shortener.news_url_shortener.service;

import com.news.url.shortener.news_url_shortener.entity.UrlMapping;
import org.springframework.stereotype.Service;


@Service
public interface UrlService {

    UrlMapping createShortUrl(String longUrl);

    String redirect(String shortCode);


}
