package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.dto.NewsDto;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.mapper.NewsMapper;
import com.newshunter.news_fetcher_service.utiltis.KeysConstraint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class NewsCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public NewsCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // check if the news is duplicate by checking if the key exists in Redis
    public boolean isDuplicate(News news) {
        String key = buildKey(news);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));

        log.info("Checking for duplicate news with key: {}", key.substring(KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX.length())); )
    }

    // build a unique key for each news item based on its guid and title
    String buildKey(News news) {
        return KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX + Integer.toHexString((news.getGuid() + news.getTitle()).hashCode());
    }

    // save the news item to Redis with a TTL
    public void save(News news, long ttlHours) {
        String key = buildKey(news);
        String value = objectMapper.writeValueAsString(news);
        redisTemplate.opsForValue().set(key, value, ttlHours, TimeUnit.HOURS);

        log.info("Saved news to cache with key: {}, TTL: {} hours", key.substring(KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX.length()), ttlHours); )

    }


    // get all news items from Redis
    public List<NewsDto> getNewsFromCache() {
        Set<String> keys = redisTemplate.keys(KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX + "*");

        log.info("Getting news from cache, found {} keys", keys != null ? keys.size() : 0);

        return redisTemplate.opsForValue()
                .multiGet(keys).stream()
                .filter(Objects::nonNull)
                .map(obj -> objectMapper.readValue(obj.toString(), News.class))
                .map(NewsMapper::mapToDto).toList();

        log.info("Successfully retrieved news from cache, total {} items", redisTemplate.opsForValue().multiGet(keys).size()); )
        ;

    }

    // delete all news items from Redis
    public void deleteNewsFromCache() {
        Set<String> keys = redisTemplate.keys(KeysConstraint.NEWS_DEDUPLICATION_KEY_PREFIX + "*");

        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            log.info("Successfully deleted news from cache, total {} items deleted", keys.size());
        }
    }
}