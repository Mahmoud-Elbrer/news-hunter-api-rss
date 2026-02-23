package com.news.url.shortener.news_url_shortener.event;


import com.news.url.shortener.news_url_shortener.dto.UrlRequestDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class UrlProducer {
    private Logger LOGGER = LoggerFactory.getLogger(UrlProducer.class);

    private NewTopic topic;

    private KafkaTemplate<String, String> kafkaTemplate;

    public UrlProducer(NewTopic topic, KafkaTemplate<String, String> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(UrlRequestDto event) {
        LOGGER.info("Message sent -> " + event.toString());

        // create message
        Message<UrlRequestDto> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        // send message
        kafkaTemplate.send(message);
    }
}