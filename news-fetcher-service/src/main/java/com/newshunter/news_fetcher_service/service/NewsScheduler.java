package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.config.RssSourcesUrls;
import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.entity.RssSource;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class NewsScheduler {
    private final ExecutorService executorService;
    private final FetcherNewsService fetcherNewsService;
    private final RateLimiterService rateLimiterService;

    public NewsScheduler(
            ExecutorService executorService,
            FetcherNewsService fetcherNewsService,
            RateLimiterService rateLimiterService
    ) {
        this.executorService = executorService;
        this.fetcherNewsService = fetcherNewsService;
        this.rateLimiterService = rateLimiterService;
    }


    @PostConstruct
    public void start() {
        // Single scheduler thread (controls timing only)
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::schedule, 0, 60, TimeUnit.SECONDS);
    }

    private void schedule() {


        for (RssSource url : RssSourcesUrls.RSS_URLS) {

            ///  System.out.println("i am running this : " + url.getUrl() + " -  every : " + url.getFetchIntervalMinutes());

            // setup 1  Dynamic Scheduling
            if (!url.isReadyToFetch()) {
                continue;
            }

            // setup 2  Rate Limiting
            if (!rateLimiterService.isAllowed(url)) {
                System.out.println("\u001B[0m" + "Rate limit reached for: " + url.getUrl() + "\u001B[0m");
                continue;
            }


            // setup 3 Execute fetch asynchronously
            executorService.submit(() -> {
                try {
                    List<News> newsItems = fetcherNewsService.fetchRssItems(url.getUrl(), url.getFetchIntervalMinutes());
                    // todo : send to cashing

                    url.markFetched();
                } catch (Exception e) {
                    url.markFailed();
                    System.err.println("Failed to fetch: " + url.getUrl());
                }
            });
        }
    }

}
