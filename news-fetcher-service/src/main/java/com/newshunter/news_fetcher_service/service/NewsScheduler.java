package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.config.RssSourcesUrls;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.entity.RssSource;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class NewsScheduler {
    private final ExecutorService executorService;
    private final NewsFetcherService newsFetcherService;
    private final RateLimiterService rateLimiterService;

    public NewsScheduler(
            ExecutorService executorService,
            NewsFetcherService newsFetcherService,
            RateLimiterService rateLimiterService
    ) {
        this.executorService = executorService;
        this.newsFetcherService = newsFetcherService;
        this.rateLimiterService = rateLimiterService;
    }


    @PostConstruct
    public void start() {
        // Single scheduler thread (controls timing only)
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::schedule, 0, 60, TimeUnit.SECONDS);
    }

    // This method is called every minute by the scheduler thread to check which RSS feeds
    private void schedule() {


        for (RssSource url : RssSourcesUrls.RSS_URLS) {

            log.info("Scheduling fetch for URL: {}, Fetch Interval: {} minutes", url.getUrl(), url.getFetchIntervalMinutes());

            // setup 1  Dynamic Scheduling
            if (!url.isReadyToFetch()) {
                log.info("Not time to fetch yet for URL: {}, next fetch in {} seconds", url.getUrl(), url.getFetchIntervalMinutes());
                continue;
            }

            // setup 2  Rate Limiting
            if (!rateLimiterService.isAllowed(url)) {
                log.info("Rate limit reached for URL: {}, skipping fetch", url.getUrl());
                continue;
            }


            // setup 3 Execute fetch asynchronously
            executorService.submit(() -> {
                try {
                    List<News> newsItems = newsFetcherService.fetchRssItems(url);
                    // todo : send to cashing

                    url.markFetched();

                    log.info("Successfully fetched {} news items from URL: {}", newsItems.size(), url.getUrl());
                } catch (Exception e) {
                    url.markFailed();
                    log.error("Error fetching URL: {}, error: {}", url.getUrl(), e.getMessage());
                }
            });
        }
    }

}
