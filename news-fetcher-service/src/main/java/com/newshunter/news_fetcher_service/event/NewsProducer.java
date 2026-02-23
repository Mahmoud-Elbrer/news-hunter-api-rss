package com.newshunter.news_fetcher_service.event;


import com.newshunter.news_fetcher_service.entity.News;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;


@Service
public class NewsProducer {
    private Logger LOGGER = LoggerFactory.getLogger(NewsProducer.class);

    private NewTopic topic;

    private KafkaTemplate<String, String> kafkaTemplate;

    public NewsProducer(NewTopic topic, KafkaTemplate<String, String> kafkaTemplate) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(News event) {
        LOGGER.info("Message sent -> " + event.toString());

        // create message
        Message<News> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, topic.name())
                .build();

        // send message
        kafkaTemplate.send(message);
    }
}
