package com.newshunter.news_fetcher_service.config;


import com.newshunter.news_fetcher_service.entity.RssSource;
import com.newshunter.news_fetcher_service.utiltis.RssPriorityConstraint;

import java.util.ArrayList;
import java.util.List;

public final class RssSourcesUrls {

    public static final List<RssSource> RSS_URLS = new ArrayList<>(List.of(
            new RssSource("https://arabic.cnn.com/rss.xml", RssPriorityConstraint.CRITICAL.getId(), 2, 5, 2),
            new RssSource("https://www.ajplus.net/stories?format=rss", RssPriorityConstraint.CRITICAL.getId(), 10, 60, 5),
            new RssSource("https://news.google.com/news?cf=all&hl=en&pz=1&ned=ca&output=rss", RssPriorityConstraint.CRITICAL.getId(), 10, 60, 5),
            new RssSource("https://www.alarabiya.net/feed/rss2/ar.xml", RssPriorityConstraint.CRITICAL.getId(), 10, 60, 12),
            new RssSource("https://assabah.ma/feed", RssPriorityConstraint.CRITICAL.getId(), 10, 60, 24),
            new RssSource("https://anyrss.com/2sIaIXHUKlM", RssPriorityConstraint.CRITICAL.getId(), 10, 60, 24)
            // todo :
            // this rss generator https://anyrss.com/2sIaIXHUKlM for this website https://www.presidency.eg/ar/
            // try to fix it in cache and image
    ));


}
