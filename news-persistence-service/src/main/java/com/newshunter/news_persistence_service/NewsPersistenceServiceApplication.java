package com.newshunter.news_persistence_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableEurekaServer
public class NewsPersistenceServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NewsPersistenceServiceApplication.class, args);
	}
}