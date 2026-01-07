package com.newshunter.news_fetcher_service.config;


import com.newshunter.news_fetcher_service.entity.RssSource;

import java.util.ArrayList;
import java.util.List;

public final class RssSourcesUrls {


    public static final List<RssSource> RSS_URLS = new ArrayList<>(List.of(
            new RssSource("https://arabic.cnn.com/rss.xml", 1),   // High
            new RssSource("https://www.aljazeera.net/aljazeera/rss.xml", 3),
            new RssSource("https://www.youm7.com/rss", 5),       // Medium
            new RssSource("https://www.alarabiya.net/saudi/rss.xml", 10),
            new RssSource("https://assabah.ma/feed", 15)       // Low
    ));


}
