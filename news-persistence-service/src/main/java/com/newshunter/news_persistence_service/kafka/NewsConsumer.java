package com.newshunter.news_persistence_service.kafka;

import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_persistence_service.mapper.NewsMapper;
import com.newshunter.news_persistence_service.repository.NewsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class NewsConsumer {


    private final NewsRepository newsRepository;

    // Batch buffer to collect messages before inserting
    // private final List<com.newshunter.news_persistence_service.entity.News> batchBuffer = new ArrayList<>();

    // Thread-safe list
    private final List<com.newshunter.news_persistence_service.entity.News> batchBuffer = Collections.synchronizedList(new ArrayList<>());

    // Define batch size (you can tune this based on load)
    private static final int BATCH_SIZE = 100;

    public NewsConsumer(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(NewsConsumer.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    // this News Entity is same as the one in news_fetcher_service
    public void consume(News news) {
        LOGGER.info("Message received in News Persistence Service :  -> " + news);

        // Map fetcher News to persistence News
        com.newshunter.news_persistence_service.entity.News persistenceNews = NewsMapper.mapToPersistenceNews(news);

        // Add entity to batch buffer instead of saving immediately
        batchBuffer.add(persistenceNews);

        // When buffer reaches defined batch size → insert all at once
        if (batchBuffer.size() >= BATCH_SIZE) {

            // Clear buffer after successful insert
            newsRepository.saveAll(batchBuffer);

            // Clear buffer after successful insert
            batchBuffer.clear();

        }

        // todo : if size not reached
        // make Scheduled
    }


}
