package com.news.url.shortener.news_url_shortener.service.GenerateShortUrlServiceImpl;

import com.news.url.shortener.news_url_shortener.entity.UrlMapping;
import com.news.url.shortener.news_url_shortener.repository.UrlRepository;
import com.news.url.shortener.news_url_shortener.service.UrlService;
import com.news.url.shortener.news_url_shortener.service.CacheService;
import com.news.url.shortener.news_url_shortener.utils.ShortCodeGenerator;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UrlServiceImpl implements UrlService {

    private final UrlRepository repository;

    private final CacheService cacheService;

    private ShortCodeGenerator shortCodeGenerator ;

    public UrlServiceImpl(UrlRepository repository, CacheService cacheService, ShortCodeGenerator shortCodeGenerator) {
        this.repository = repository;
        this.cacheService = cacheService;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    @Override
    public UrlMapping createShortUrl(String longUrl) {

        // Check if URL already exists
        Optional<UrlMapping> existing = repository.findByLongUrl(longUrl);
        if (existing.isPresent()) return existing.get();

        String shortCode = shortCodeGenerator.generateShortCode();

        UrlMapping mapping = new UrlMapping();
        mapping.setShortCode(shortCode);
        mapping.setLongUrl(longUrl);

        // save to redis
        cacheService.save(mapping);

        // save to db
        return repository.save(mapping);
    }

    @Override
    public String redirect(String code) {

        // Check short url form cache
        String longUrl = cacheService.getLongUrl(code);

        if (longUrl != null) {
            // increment Click to cache
            cacheService.incrementClick(code);
            return longUrl;
        }

        // check from db
        UrlMapping mapping = repository.findByShortCode(code);

        // save it cache
        cacheService.save(mapping);

        // get increment and increment and update it
        mapping.setClickCount(mapping.getClickCount() + 1);
        repository.save(mapping);

        return mapping.getLongUrl();
    }
}

