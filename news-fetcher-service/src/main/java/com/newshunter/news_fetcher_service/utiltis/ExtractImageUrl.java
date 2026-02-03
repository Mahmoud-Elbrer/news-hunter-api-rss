package com.newshunter.news_fetcher_service.utiltis;

import com.rometools.rome.feed.synd.SyndEntry;
import org.jdom2.Element;

import java.util.List;

public class ExtractImageUrl {

    private static final String MEDIA_NS = "http://search.yahoo.com/mrss/";

    public static String extractImageUrlFromEntry(SyndEntry entry) {

        // Here Read <media:content> and get Image
        List<Element> foreignMarkup = entry.getForeignMarkup();
        if (foreignMarkup != null) {
            for (Element el : foreignMarkup) {
                if ("content".equals(el.getName()) && MEDIA_NS.equals(el.getNamespaceURI())) {

                    String url = el.getAttributeValue("url");
                    if (url != null && !url.isBlank()) {
                        return url;
                    }
                }
            }
        }

        return null;
    }
}
