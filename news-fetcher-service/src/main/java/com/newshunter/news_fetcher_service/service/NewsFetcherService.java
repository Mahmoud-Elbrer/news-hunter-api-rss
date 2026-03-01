package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.entity.RssSource;
import com.newshunter.news_fetcher_service.event.NewsProducer;
import com.newshunter.news_fetcher_service.utiltis.Constraint;
import com.newshunter.news_fetcher_service.utiltis.ExtractImageUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.rometools.rome.io.XmlReader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

@Service
@Slf4j
public class NewsFetcherService {

    private final NewsCacheService newsCacheService;

    private final NewsProducer newsProducer;

    public NewsFetcherService(NewsCacheService newsCacheService, NewsProducer newsProducer) {
        this.newsCacheService = newsCacheService;
        this.newsProducer = newsProducer;
    }


    // todo : add retry mechanism for fetching feedUrl after some time if it fails
    // todo : add circuit breaker mechanism for feedUrl that fails repeatedly to avoid overwhelming the news fetcher service and the news sites
    // todo : add monitoring and alerting for feedUrl that fails repeatedly to notify the team about potential issues with the news sites or the news fetcher service
    // fetch news items from the given RSS feed URL and return a list of news items
    public List<News> fetchRssItems(RssSource rssSource) {

        //System.out.println("\u001B[32m" + "Fetching : "  + rssSource.getUrl() +" ttl:"+ rssSource.getTtlHOURS() + " Hour for every " + rssSource.getFetchIntervalMinutes() + " minutes" + "\u001B[0m");

        log.info("Fetching RSS feed from URL: {}, TTL: {} hours, Fetch Interval: {} minutes", rssSource.getUrl(), rssSource.getTtlHOURS(), rssSource.getFetchIntervalMinutes());

        List<News> items = new ArrayList<>();


//        ObjectMapper objectMapper = new ObjectMapper();

        try {

            URL url = new URL(rssSource.getUrl());
            // connect to the URL with a timeout and user-agent to avoid 403 errors from news sites
            URLConnection connection = url.openConnection();

            // It is very important to avoid 403 news sites
            // connection.setRequestProperty("User-Agent", "NewsHunterBot/1.0");
            connection.setRequestProperty("User-Agent", Constraint.USER_AGENT);
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            // stream the feed data and parse it using Rome's SyndFeedInput
            try (InputStream inputStream = connection.getInputStream(); XmlReader reader = new XmlReader(inputStream)) {

                log.info("Successfully connected to URL: {}", rssSource.getUrl());

                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(reader);

                // process each entry in the feed and create a News item for each entry, then check for duplicates and save to cache and send to Kafka
                for (SyndEntry entry : feed.getEntries()) {

                    log.info("Processing entry: {} from feed: {}", entry.getTitle(), feed.getTitle());

                    News item = new News();
                    item.setGuid(entry.getUri() != null ? entry.getUri() : entry.getLink());
                    item.setTitle(entry.getTitle());
                    item.setLink(entry.getLink());
                    item.setDescription(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                    item.setPubDate(entry.getPublishedDate());
                    item.setSource(feed.getTitle());
                    item.setImageUrl(ExtractImageUrl.extractImageUrlFromEntry(entry));

//                    String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
//                    System.out.println(json);

                    // Deduplication Check
                    if (newsCacheService.isDuplicate(item)) {
                        log.info("Duplicate news found, skipping: {}", item.getTitle());
                        continue;
                    }

                    // New News to Cache
                    newsCacheService.save(item, rssSource.getTtlHOURS());
                    log.info("Saved news to cache: {}", item.getTitle());


                    // Send to Kafka
                    newsProducer.sendMessage(item);
                    log.info("Sent news to Kafka: {}", item.getTitle());

                    items.add(item);
                }
            }

        } catch (Exception e) {
            // todo : log error
            // todo : retry fetch feedUrl after some time
            log.error("Error fetching RSS feed from URL: {}. Error: {}", rssSource.getUrl(), e.getMessage());
            throw new RuntimeException(e); // this url : feedUrl filer
        }

        log.info("Finished fetching RSS feed from URL: {}, total new items fetched: {}", rssSource.getUrl(), items.size());
        return items;
    }

}
