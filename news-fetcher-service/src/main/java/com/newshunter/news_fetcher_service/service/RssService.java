package com.newshunter.news_fetcher_service.service;

import com.newshunter.news_fetcher_service.entity.RssItem;
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
public class RssService {

    // todo : Get this data for channels db
    private static final List<String> RSS_URLS = List.of(
            "https://www.alarabiya.net/.mrss/mrss.xml",
            "https://aawsat.com/feed/all",
            "https://aawsat.com/feed/arab-world",
            "https://arabic.cnn.com/rss.xml"
    );


    public void fetchAllRssItems() {
        List<RssItem> allItems = new ArrayList<>();
        for (String url : RSS_URLS) {
            try {
                allItems.addAll(fetchRssItems(url));
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private List<RssItem> fetchRssItems(String feedUrl) throws Exception {

        URL url = new URL(feedUrl);
        URLConnection connection = url.openConnection();

        // It is very important to avoid 403 news sites
        //  connection.setRequestProperty("User-Agent", "NewsHunterBot/1.0");

        try (InputStream inputStream = connection.getInputStream(); XmlReader reader = new XmlReader(inputStream)) {

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(reader);

            List<RssItem> items = new ArrayList<>();

            ObjectMapper objectMapper = new ObjectMapper();


            for (SyndEntry entry : feed.getEntries()) {

                RssItem item = new RssItem();
                item.setTitle(entry.getTitle());
                item.setLink(entry.getLink());
                item.setDescription(entry.getDescription() != null ? entry.getDescription().getValue() : "");
                item.setPubDate(entry.getPublishedDate());
                item.setSource(feed.getTitle());

                String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(item);

                System.out.println(json);


                items.add(item);
            }

            return items;
        }
    }

}