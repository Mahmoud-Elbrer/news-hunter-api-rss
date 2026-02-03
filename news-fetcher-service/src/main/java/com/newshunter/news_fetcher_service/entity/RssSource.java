package com.newshunter.news_fetcher_service.entity;

import lombok.Getter;

@Getter
public class RssSource {

    private final String url;


    // Dynamic scheduling
    private int fetchIntervalMinutes;
    private long lastFetchedAtMinutes;
    private int failureCount = 0;

    // Rate limiting (per RSS source)
    private final int maxRequests;     // max requests allowed
    private final int rateLimitWindowMinutes;   // time window in minutes

    // TTL per source (in hours) to delete data from cache
    private final long ttlHOURS;


    /**
     * Sets lastFetchedAtMinutes to current time to prevent immediate fetch on app start
     * this so important
     */
    public RssSource(String url, int fetchIntervalMinutes, int maxRequests, int rateLimitWindowMinutes , long ttlHOURS) {
        this.url = url;
        this.fetchIntervalMinutes = fetchIntervalMinutes;
        this.lastFetchedAtMinutes = currentMinutes();
        // Rate limiting
        this.maxRequests = maxRequests;
        this.rateLimitWindowMinutes = rateLimitWindowMinutes;
        this.ttlHOURS = ttlHOURS;
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
