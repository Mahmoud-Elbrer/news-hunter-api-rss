package com.news.url.shortener.news_url_shortener.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "shorted_urls")
public class UrlMapping {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false , unique = true , length = 2048)
    private String longUrl;

    @Column(nullable = false, unique = true )
    private String shortCode;

    @Column(name = "click_count")
    private int clickCount = 0;

    @Column(name = "expiresAt")
    private LocalDateTime expiresAt;


}
