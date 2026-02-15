package com.newshunter.news_persistence_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NewsPersistenceServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NewsPersistenceServiceApplication.class, args);
	}
}