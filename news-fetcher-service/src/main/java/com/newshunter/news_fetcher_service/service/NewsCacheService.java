package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.dto.NewsDto;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.mapper.NewsMapper;
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

    private static final long TTL_HOURS = 6;

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

    public void save(News news , int ttlHours) {

        System.out.println(news.getLink());
        System.out.println(ttlHours);

        String key = buildKey(news);
        String value = objectMapper.writeValueAsString(news);
        // todo : make TTL depend on time Priority News Time * 1 days
        redisTemplate.opsForValue().set(key, value, ttlHours, TimeUnit.MINUTES);
    }


    public List<NewsDto> getNewsFromCache() {
        Set<String> keys = redisTemplate.keys("news:deduplication:*");

        return redisTemplate.opsForValue()
                .multiGet(keys).stream()
                .filter(Objects::nonNull)
                .map(obj -> objectMapper.readValue(obj.toString(), News.class))
                .map(NewsMapper::mapToDto).toList();
    }


}
