package com.newshunter.news_persistence_service.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String guid;

    @Column(name = "title", nullable = false ,columnDefinition = "TEXT")
    private String title;

    //@Column(name = "link", nullable = false, unique = true)
    @Column(name = "link", nullable = false  ,columnDefinition = "TEXT")
    private String link;

    @Column(name = "description"  ,columnDefinition = "TEXT")
    private String description;

    @Column(name = "pubDate")
    private Date pubDate;

    @Column(name = "source" ,columnDefinition = "TEXT")
    private String source;

    @Column(name = "imageUrl" ,columnDefinition = "TEXT")
    private String imageUrl;
}
