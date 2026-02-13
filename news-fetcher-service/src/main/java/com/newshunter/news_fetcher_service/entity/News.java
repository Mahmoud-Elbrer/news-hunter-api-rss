package com.newshunter.news_fetcher_service.entity;

import lombok.*;
import org.w3c.dom.Text;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class News {

    private String guid;

    private String title;

    private String link;

    private String description;

    private Date pubDate;

    private String source;

    private String imageUrl;
}