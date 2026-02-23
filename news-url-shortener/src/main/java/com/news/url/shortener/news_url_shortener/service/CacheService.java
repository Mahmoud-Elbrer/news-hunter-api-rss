package com.news.url.shortener.news_url_shortener.service;

import com.news.url.shortener.news_url_shortener.entity.UrlMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public CacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    String buildKey(String shortCode) {
        return "shorturls:code:" + shortCode;
    }

    public void save(UrlMapping urlMapping) {
        String key = buildKey(urlMapping.getShortCode());

        redisTemplate.opsForHash().put(key, "id", urlMapping.getId());
        redisTemplate.opsForHash().put(key, "longUrl", urlMapping.getLongUrl());
        redisTemplate.opsForHash().put(key, "shortCode", urlMapping.getShortCode());
        redisTemplate.opsForHash().put(key, "clickCount", urlMapping.getClickCount());

        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }


    public String getLongUrl(String shortCode) {

        String key = buildKey(shortCode);

        Object longUrl = redisTemplate.opsForHash().get(key, "longUrl");

        return longUrl != null ? longUrl.toString() : null;
    }


    public void incrementClick(String shortCode) {
        String key = buildKey(shortCode);

        redisTemplate.opsForHash().increment(key, "clickCount", 1);
    }


    //    public UrlMapping getByShortCode(String shortCode) throws Exception {
//        String key = buildKey(shortCode);
//
//        Object value = redisTemplate.opsForValue().get(key);
//        if (value == null) return null;
//
//        return objectMapper.readValue(value.toString(), UrlMapping.class);
//    }

}