package com.news.url.shortener.news_url_shortener.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedirectUrlResponse {

    private boolean success;
    private String originalUrl;
    private String code;

}
