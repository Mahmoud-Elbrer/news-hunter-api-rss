package com.news.url.shortener.news_url_shortener.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlRequestDto {


    //@NotBlank(message = "URL cannot be empty or null")
    @NotEmpty(message = "URL cannot be empty or null")
    @URL(protocol = "https", message = "Invalid URL. Must start with http:// or https://")
    private String longUrl;
}
