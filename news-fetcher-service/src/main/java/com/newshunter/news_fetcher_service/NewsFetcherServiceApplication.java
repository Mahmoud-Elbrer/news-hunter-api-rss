package com.newshunter.news_fetcher_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEurekaServer
public class NewsFetcherServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsFetcherServiceApplication.class, args);
	}

}
