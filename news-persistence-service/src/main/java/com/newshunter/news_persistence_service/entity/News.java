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
    private Long Id;

    @Column(name = "title", nullable = false)
    private String title;

    //@Column(name = "link", nullable = false, unique = true)
    @Column(name = "link", nullable = false )
    private String link;

    @Column(name = "description")
    private String description;

    @Column(name = "pubDate")
    private Date pubDate;

    @Column(name = "source")
    private String source;

    @Column(name = "imageUrl")
    private String imageUrl;
}
