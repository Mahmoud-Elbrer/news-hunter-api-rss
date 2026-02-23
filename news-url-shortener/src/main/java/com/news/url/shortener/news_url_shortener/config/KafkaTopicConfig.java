package com.news.url.shortener.news_url_shortener.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.name}")
    private String TOPIC_NAME;

    @Bean
    public NewTopic newsTopic() {
        return TopicBuilder.name(TOPIC_NAME)
                .build();
    }

}

// to run kafka
// kafka-server-start /opt/homebrew/etc/kafka/server.properties
