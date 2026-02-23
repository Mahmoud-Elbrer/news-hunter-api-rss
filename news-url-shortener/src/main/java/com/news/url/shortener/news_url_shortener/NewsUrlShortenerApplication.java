package com.news.url.shortener.news_url_shortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsUrlShortenerApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsUrlShortenerApplication.class, args);
	}

}