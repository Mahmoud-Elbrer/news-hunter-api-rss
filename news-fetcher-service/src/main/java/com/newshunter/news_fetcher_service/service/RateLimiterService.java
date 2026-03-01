package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.entity.RateLimitState;
import com.newshunter.news_fetcher_service.entity.RssSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class RateLimiterService {
    private final Map<String, RateLimitState> rateLimitMap = new ConcurrentHashMap<>();


    // Check if a request is allowed based on the rate limit for the given RSS source
    public boolean isAllowed(RssSource source) {

        long nowMinutes = currentMinutes();

        RateLimitState state = rateLimitMap.computeIfAbsent(
                source.getUrl(),
                k -> new RateLimitState(nowMinutes)
        );

        log.info("Checking rate limit for URL: {}, Current Count: {}, Window Start: {}, Minutes Passed: {}",
                source.getUrl(), state.getRequestCount(), state.getWindowStartMinutes(), nowMinutes - state.getWindowStartMinutes());

        long minutesPassed = nowMinutes - state.getWindowStartMinutes();

        // New window → reset counter
        if (minutesPassed >= source.getRateLimitWindowMinutes()) {
            state.reset(nowMinutes);
        }

        // Limit reached
        if (state.getRequestCount() >= source.getMaxRequests()) {
            return false;
        }

        // Allow request
        state.increment();

        log.info("Allowed request for URL: {}, Updated Count: {}", source.getUrl(), state.getRequestCount());

        return true;

    }

    // Helper method to get the current time in minutes since epoch
    private long currentMinutes() {
        return System.currentTimeMillis() / (1000 * 60);
    }

}
