package com.news.url.shortener.news_url_shortener.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ShortCodeNotFoundException extends RuntimeException {

    public ShortCodeNotFoundException(String code) {
        super("Short code not found: " + code);
    }

}
