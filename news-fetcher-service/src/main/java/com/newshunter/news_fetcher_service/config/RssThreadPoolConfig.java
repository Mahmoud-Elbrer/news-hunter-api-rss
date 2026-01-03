package com.newshunter.news_fetcher_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class RssThreadPoolConfig {

    @Bean
    public ExecutorService rssExecutorService() {
        // 4 Thread Pool
        return Executors.newFixedThreadPool(4);
    }
}
