package com.news.url.shortener.news_url_shortener.event;


import com.news.url.shortener.news_url_shortener.dto.UrlRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UrlConsumer {

    private final static Logger LOGGER = LoggerFactory.getLogger(UrlConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    // this News Entity is same as the one in news_fetcher_service
    public void consume(UrlRequestDto urlRequestDto) {
        LOGGER.info("Message received in News Persistence Service :  -> " , urlRequestDto.toString());

    }



}
