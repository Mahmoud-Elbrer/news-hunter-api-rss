package com.newshunter.news_fetcher_service.config;


import com.newshunter.news_fetcher_service.entity.RssSource;
import com.newshunter.news_fetcher_service.utiltis.RssPriorityConstraint;

import java.util.ArrayList;
import java.util.List;

public final class RssSourcesUrls {

    public static final List<RssSource> RSS_URLS = new ArrayList<>(List.of(
            new RssSource("https://arabic.cnn.com/rss.xml", RssPriorityConstraint.CRITICAL.getId(), 2, 5),
            new RssSource("https://www.aljazeera.net/aljazeera/rss.xml", RssPriorityConstraint.MEDIUM.getId(), 10, 60),
            new RssSource("https://www.youm7.com/rss", RssPriorityConstraint.MEDIUM.getId(), 10, 60),
            new RssSource("https://www.alarabiya.net/saudi/rss.xml", RssPriorityConstraint.VERY_LOW.getId(), 10, 60),
            new RssSource("https://assabah.ma/feed", RssPriorityConstraint.LOW.getId(), 10, 60)
    ));


}
