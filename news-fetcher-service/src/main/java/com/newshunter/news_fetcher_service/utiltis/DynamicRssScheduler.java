package com.newshunter.news_fetcher_service.utiltis;

import com.newshunter.news_fetcher_service.config.RssSourcesUrls;
import com.newshunter.news_fetcher_service.entity.NewsItem;
import com.newshunter.news_fetcher_service.entity.RssSource;
import com.newshunter.news_fetcher_service.service.RssService;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class DynamicRssScheduler {
    private final ExecutorService executorService;
    private final RssService rssService;

    public DynamicRssScheduler(
            ExecutorService executorService,
            RssService rssService
    ) {
        this.executorService = executorService;
        this.rssService = rssService;
    }


    @PostConstruct
    public void start() {
        // Single scheduler thread (controls timing only)
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::schedule, 0, 60, TimeUnit.SECONDS);
    }

    private void schedule() {


        for (RssSource url : RssSourcesUrls.RSS_URLS) {

          ///  System.out.println("i am running this : " + url.getUrl() + " -  every : " + url.getFetchIntervalMinutes());

            if (!url.isReadyToFetch()) {
                continue;
            }

            executorService.submit(() -> {
                try {
                    List<NewsItem> newsItems = rssService.fetchRssItems(url.getUrl() , url.getFetchIntervalMinutes());
                    url.markFetched();
                } catch (Exception e) {
                    url.markFailed();
                    System.err.println("Failed to fetch: " + url.getUrl());
                }
            });
        }
    }

}
