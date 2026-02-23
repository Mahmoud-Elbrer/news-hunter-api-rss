package com.news.url.shortener.news_url_shortener.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShortCodeGenerator {

    private final StringRedisTemplate redisTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShortCodeGenerator.class);


    public ShortCodeGenerator(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final String KEY_POOL_SET = "shortener:key_pool";


    public String generateShortCode() {

        // Try to get a short code from the pool
        String shortCode = redisTemplate.opsForSet().pop(KEY_POOL_SET);


        if (shortCode != null) {
            return shortCode;
        }

        // If the pool is empty, generate a new short code directly (fallback)
        return generateRandom(8);
    }

    public String generateRandom(int length) {

        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // todo: optimize to base62 encoding of an incrementing counter to ensure uniqueness and avoid collisions
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            key.append(characters.charAt(index));
        }
        return key.toString();

    }


}