package com.news.url.shortener.news_url_shortener.repository;

import com.news.url.shortener.news_url_shortener.entity.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<UrlMapping, Long> {

    UrlMapping findByShortCode(String code);
    Optional<UrlMapping> findByLongUrl(String shortCode);

}