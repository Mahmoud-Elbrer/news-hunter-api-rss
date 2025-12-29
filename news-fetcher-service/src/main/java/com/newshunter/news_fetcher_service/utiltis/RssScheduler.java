package com.newshunter.news_fetcher_service.utiltis;

import com.newshunter.news_fetcher_service.service.RssService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RssScheduler {
    private final RssService rssService;

    public RssScheduler(RssService rssService) {
        this.rssService =
                rssService;
    }

    //@Scheduled(fixedDelay = 300000)
    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void fetchUpdates() {
        rssService.fetchAllRssItems();
    }
}
