package com.newshunter.news_persistence_service.repository;

import com.newshunter.news_persistence_service.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
