package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.entity.RateLimitState;
import com.newshunter.news_fetcher_service.entity.RssSource;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {
    private final Map<String , RateLimitState> rateLimitMap =  new ConcurrentHashMap<>();

    public boolean isAllowed(RssSource source){

        long nowMinutes  =  currentMinutes() ;

        RateLimitState state = rateLimitMap.computeIfAbsent(
                source.getUrl(),
                k -> new RateLimitState(nowMinutes)
        );

        long minutesPassed =  nowMinutes -  state.getWindowStartMinutes() ;

        // New window → reset counter
        if(minutesPassed >= source.getRateLimitWindowMinutes()) {
            state.reset(nowMinutes);
        }

        // Limit reached
        if(state.getRequestCount() >= source.getMaxRequests()) {
            return  false ;
        }

        // Allow request
        state.increment();

        return true ;

    }

    private long currentMinutes() {
        return System.currentTimeMillis() / (1000 * 60);
    }

}
