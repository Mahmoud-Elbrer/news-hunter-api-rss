package com.news.url.shortener.news_url_shortener.service;

import com.news.url.shortener.news_url_shortener.dto.UrlRequestDto;
import com.news.url.shortener.news_url_shortener.entity.UrlMapping;

public interface UrlService {

    UrlMapping createShortUrl(String longUrl);

    String redirect(String shortCode);


}
