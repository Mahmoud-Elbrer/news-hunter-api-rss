package com.news.url.shortener.news_url_shortener.utils;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;


@Service
public class KeyPoolManager {

    private final StringRedisTemplate redisTemplate;
    private final RedissonClient redissonClient;

    private final ShortCodeGenerator shortCodeGenerator;

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyPoolManager.class);


    public KeyPoolManager(StringRedisTemplate redisTemplate, RedissonClient redissonClient, ShortCodeGenerator shortCodeGenerator) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.shortCodeGenerator = shortCodeGenerator;
    }


    // key pool set to store pre-generated keys in Redis
    private static final String KEY_POOL_SET = "shortener:key_pool";

    // key counter to keep track of the last generated key ID
    private static final String KEY_COUNTER = "shortener:key_counter";

    // key_gen_lock to ensure only one instance generates keys at a time
    private static final String KEY_GEN_LOCK = "shortener:key_gen_lock";


    private static final int MIN_KEY_POOL_SIZE = 10000;
    private static final int GENERATE_BATCH_SIZE = 50000;
    private static final int SHORT_CODE_LENGTH = 8;


    // Scheduled to run periodically to check and replenish the key pool
    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.MINUTES) // Every 5 minutes
    public void replenishKeyPool() {

        // lock to ensure only one instance generates keys at a time
        RLock lock = redissonClient.getLock(KEY_GEN_LOCK);
        try {

            // Acquire lock with a timeout to prevent deadlocks
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {

                // check current pool size
                Long currentPoolSize = redisTemplate.opsForSet().size(KEY_POOL_SET);

                if (currentPoolSize == null || currentPoolSize < MIN_KEY_POOL_SIZE) {
                    // generate and add keys to the pool
                    generateAndAddKeys();
                }

            } else {
                LOGGER.info("Could not acquire key generation lock. Another instance might be generating keys.");
            }
        } catch (Exception e) {
            LOGGER.error("Error during key pool replenishment: {}", e.getStackTrace());
            // e.printStackTrace();
        } finally {
            if (lock.isHeldByCurrentThread()) {
                // unlock to allow other instances to generate keys
                lock.unlock();
            }
        }

    }

    private void generateAndAddKeys() {

        // use a set to avoid duplicates in the generated keys
        Set<String> newKeys = new java.util.HashSet<>();

        // get the next ID to generate from the counter and increment it by the batch size
        long startId = redisTemplate.opsForValue().increment(KEY_COUNTER, GENERATE_BATCH_SIZE) - GENERATE_BATCH_SIZE;


        for (int i = 0; i < GENERATE_BATCH_SIZE; i++) {
            // todo : improver to base62 encoding
            // it best from math random ans fast
            // todo : add counter to generate sequential keys and avoid duplicates
            String newKey = shortCodeGenerator.generateShortCode();
            newKeys.add(newKey);
        }


        redisTemplate.opsForSet().add(KEY_POOL_SET, newKeys.toArray(new String[0]));
        LOGGER.info("Added {} new keys to the pool.", newKeys.size());

    }


}
