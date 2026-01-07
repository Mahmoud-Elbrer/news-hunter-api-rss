package com.newshunter.news_fetcher_service.entity;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsItem {

    private String title;

    private String link;

    private String description;

    private Date pubDate;

    private String source;

    private String imageUrl;
}