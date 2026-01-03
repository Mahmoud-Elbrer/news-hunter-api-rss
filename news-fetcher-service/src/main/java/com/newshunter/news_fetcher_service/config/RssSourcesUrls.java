package com.newshunter.news_fetcher_service.config;


import java.util.List;

public final class RssSourcesUrls {

    private RssSourcesUrls() {
        // prevent instantiation
    }

    // todo : Get this data for channels db
    public static final List<String> RSS_URLS = List.of(
            "https://www.alarabiya.net/.mrss/mrss.xml",
            "https://aawsat.com/feed/all",
            "https://aawsat.com/feed/arab-world",
            "https://arabic.cnn.com/rss.xml",
            "https://www.aljazeera.net/aljazeera/rss.xml",
            "https://www.masrawy.com/rss",
            "https://www.albawabhnews.com/rss.xml",
            "https://www.elwatannews.com/rss",
            "https://www.youm7.com/rss",
            "https://www.arabstoday.net/ar/rss",
            "https://www.arabstoday.net/en/rss",
            "https://dohanews.co/feed",
            "http://dostor.org/rss.aspx",
            "https://www.alkhaleej.ae/section/1110/rss.xml",
            "https://aswat24.com/feed",
            "https://burathanews.com/rss.php",
            "https://newsabah.com/feed",
            "https://assabah.ma/feed",
            "https://aletihad.ae/rss.xml",
            "https://elnashra.com/rss.xml",
            "https://www.bna.bh/RSSFeeds.aspx?cms=iQRpheuphYtJ6pyXUGiNqqtA5DItoRVa",
            "https://rss.dw.com/syndication/feeds/MENA_RSS_GNS_AR.42103-copypaste.html",
            "https://arabfinance.com/RSS/RSSList/en",
            "https://www.ajplus.net/rss",
            "https://www.albawaba.com/rss",
            "https://www.alarabiya.net/saudi/rss.xml"
    );
}
