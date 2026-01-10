package com.newshunter.news_fetcher_service.entity;


import lombok.Getter;

@Getter
public class RateLimitState {
    private int requestCount;
    private long windowStartMinutes;

    public RateLimitState(long windowsStartMinutes) {
        this.windowStartMinutes = windowsStartMinutes;
        this.requestCount = 0;
    }

    public void increment() {
        requestCount++;
    }

    public void reset(long newWindowStartMinutes) {
        this.windowStartMinutes = newWindowStartMinutes;
        this.requestCount = 0;
    }


}

