package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.entity.News;
import org.springframework.stereotype.Service;

import com.rometools.rome.io.XmlReader;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;


import com.rometools.rome.feed.synd.*;
import com.rometools.rome.io.*;
import tools.jackson.databind.ObjectMapper;

@Service
public class FetcherNewsService {
    private static final String USER_AGENT = "NewsBot/1.0 (+https://news.com)";
    //   private static final String USER_AGENT = "NewsHunterBot/1.0 (+https://newshunter.news)";


    public List<News> fetchRssItems(String feedUrl, int minutes) {

        //  System.out.println("DONE GET : " + feedUrl + " Every : " +  minutes);
        System.out.println("\u001B[32m" + "DONE GET : " + feedUrl + " Every : " + minutes + " minutes" + "\u001B[0m");


        List<News> items = new ArrayList<>();


        ObjectMapper objectMapper = new ObjectMapper();

        try {
            URL url = new URL(feedUrl);
            URLConnection connection = url.openConnection();

            // It is very important to avoid 403 news sites
            // connection.setRequestProperty("User-Agent", "NewsHunterBot/1.0");
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setConnectTimeout(10_000);
            connection.setReadTimeout(10_000);

            try (InputStream inputStream = connection.getInputStream();
                 XmlReader reader = new XmlReader(inputStream)) {

                SyndFeedInput input = new SyndFeedInput();
                SyndFeed feed = input.build(reader);

                for (SyndEntry entry : feed.getEntries()) {

                    News item = new News();
                    item.setTitle(entry.getTitle());
                    item.setLink(entry.getLink());
                    item.setDescription(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                    item.setPubDate(entry.getPublishedDate());
                    item.setSource(feed.getTitle());

//                    String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);
//                    System.out.println(json);


                    items.add(item);
                }
            }

        } catch (Exception e) {
            // todo : enable this for error
//            System.err.println("RSS fetch failed for URL: " + feedUrl);
//            System.err.println(e.getMessage());
//            throw new RuntimeException(e);
        }

        return items;
    }

}
