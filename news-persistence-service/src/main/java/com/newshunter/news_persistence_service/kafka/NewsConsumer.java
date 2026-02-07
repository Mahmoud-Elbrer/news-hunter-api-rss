package com.newshunter.news_persistence_service.kafka;

import com.newshunter.news_fetcher_service.entity.News;
import com.newshunter.news_persistence_service.mapper.NewsMapper;
import com.newshunter.news_persistence_service.repository.NewsRepository;
import com.newshunter.news_persistence_service.service.NewsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NewsConsumer {


    private final NewsRepository newsRepository;

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

        // save news to database
        newsRepository.save(persistenceNews);

    }


}
