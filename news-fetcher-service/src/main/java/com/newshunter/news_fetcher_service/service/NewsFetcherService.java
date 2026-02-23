package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_fetcher_service.entity.RssSource;
import com.newshunter.news_fetcher_service.event.NewsProducer;
import com.newshunter.news_fetcher_service.utiltis.Constraint;
import com.newshunter.news_fetcher_service.utiltis.ExtractImageUrl;
import org.springframework.stereotype.Service;

import com.rometools.rome.io.XmlReader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;

@Service
public class NewsFetcherService {

    private final NewsCacheService newsCacheService;

    private final NewsProducer newsProducer;

    public NewsFetcherService(NewsCacheService newsCacheService, NewsProducer newsProducer) {
        this.newsCacheService = newsCacheService;
        this.newsProducer = newsProducer;
    }


    public List<News> fetchRssItems(RssSource rssSource) {

        //  System.out.println("DONE GET : " + feedUrl + " Every : " +  minutes);
        System.out.println("\u001B[32m" + "Fetching : "  + rssSource.getUrl() +" ttl:"+ rssSource.getTtlHOURS() + " Hour for every " + rssSource.getFetchIntervalMinutes() + " minutes" + "\u001B[0m");


        List<News> items = new ArrayList<>();


//        ObjectMapper objectMapper = new ObjectMapper();

        try {
            URL url = new URL(rssSource.getUrl());
            URLConnection connection = url.openConnection();

            // It is very important to avoid 403 news sites
            // connection.setRequestProperty("User-Agent", "NewsHunterBot/1.0");
            connection.setRequestProperty("User-Agent", Constraint.USER_AGENT);
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            try (InputStream inputStream = connection.getInputStream();
                 XmlReader reader = new XmlReader(inputStream)) {

                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(reader);

                for (SyndEntry entry : feed.getEntries()) {

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
                        continue;
                    }

                    // New News to Cache
                    newsCacheService.save(item, rssSource.getTtlHOURS());


                    // Send to Kafka
                    newsProducer.sendMessage(item);

                    items.add(item);
                }
            }

        } catch (Exception e) {
            // todo : log error
            // todo : retry fetch feedUrl after some time
            throw new RuntimeException(e); // this url : feedUrl filer
        }

        return items;
    }

}
