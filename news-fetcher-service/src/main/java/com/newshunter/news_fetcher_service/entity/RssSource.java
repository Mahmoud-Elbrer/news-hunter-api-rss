package com.newshunter.news_fetcher_service.entity;

import lombok.Getter;

@Getter
public class RssSource {

    private final String url;

    private int fetchIntervalMinutes;
    private long lastFetchedAtMinutes;
    private int failureCount = 0;



    /**
      Sets lastFetchedAtMinutes to current time to prevent immediate fetch on app start
     this so important
     */
    public RssSource(String url, int fetchIntervalMinutes) {
        this.url = url;
        this.fetchIntervalMinutes = fetchIntervalMinutes;
        this.lastFetchedAtMinutes = currentMinutes();
    }

    private long currentMinutes() {
        return System.currentTimeMillis() / (1000 * 60);
    }

    public boolean isReadyToFetch() {
        long minutesSinceLastFetch = currentMinutes() - lastFetchedAtMinutes;
        return minutesSinceLastFetch >= fetchIntervalMinutes;
    }

    // Resets failure count an
    // d updates lastFetchedAtMinutes
    /**
     * Marks the feed as successfully fetched
     * Resets failure count and updates lastFetchedAtMinutes
     */
    public void markFetched() {
        this.lastFetchedAtMinutes = currentMinutes();
        this.failureCount = 0;
    }


    /**
     * Marks the feed fetch as failed
     * Increments failure count and doubles fetch interval (exponential backoff), max 60 minutes
     */
    public void markFailed() {
        failureCount++;
        fetchIntervalMinutes = Math.min(fetchIntervalMinutes * 2, 60);
        this.lastFetchedAtMinutes = currentMinutes();
    }
}
