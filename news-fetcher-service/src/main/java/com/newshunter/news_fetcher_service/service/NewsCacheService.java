package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.dto.NewsDto;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.mapper.NewsMapper;
import org.jspecify.annotations.Nullable;
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

    private static final long TTL_HOURS = 10;

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
        return "news:deduplication:" + Integer.toHexString((news.getId() + news.getTitle()).hashCode());
    }

    public void save(News news) {
        String key = buildKey(news);
        String value = objectMapper.writeValueAsString(news);
        // todo : make TTL depend on time Priority News Time * 1 days
        redisTemplate.opsForValue().set(key, value, TTL_HOURS, TimeUnit.HOURS);
    }

    ObjectMapper mapper = new ObjectMapper();

    public List<NewsDto> getNewsFromCache() {
        Set<String> keys = redisTemplate.keys("news:deduplication:*");

        List<NewsDto> news = redisTemplate.opsForValue().multiGet(keys).stream()
                .filter(Objects::nonNull)
                .map(obj -> mapper.readValue(obj.toString(), News.class))
                .filter(Objects::nonNull)
                .map(NewsMapper::mapToDto)
                .toList();

        return news;
    }


}
