package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.dto.NewsDto;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.mapper.NewsMapper;
import com.newshunter.news_fetcher_service.utiltis.KeysConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class NewsCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public NewsCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isDuplicate(News news) {
        String key = buildKey(news);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    String buildKey(News news) {
        return KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX + Integer.toHexString((news.getGuid() + news.getTitle()).hashCode());
    }

    public void save(News news, long ttlHours) {
        String key = buildKey(news);
        String value = objectMapper.writeValueAsString(news);
        redisTemplate.opsForValue().set(key, value, ttlHours, TimeUnit.HOURS);
    }


    public List<NewsDto> getNewsFromCache() {
        Set<String> keys = redisTemplate.keys(KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX + "*");

        return redisTemplate.opsForValue()
                .multiGet(keys).stream()
                .filter(Objects::nonNull)
                .map(obj -> objectMapper.readValue(obj.toString(), News.class))
                .map(NewsMapper::mapToDto).toList();
    }

    public void deleteNewsFromCache() {
        Set<String> keys = redisTemplate.keys(KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}