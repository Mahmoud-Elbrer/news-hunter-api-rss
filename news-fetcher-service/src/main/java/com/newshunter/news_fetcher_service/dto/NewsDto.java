package com.newshunter.news_fetcher_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class NewsDto {
    private String title;
    private String link;
    private String description;
    private Date pubDate;
    private String source;
    private String imageUrl;
}
