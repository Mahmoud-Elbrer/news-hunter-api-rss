package com.newshunter.news_persistence_service.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsDto {


    private Long Id;

    @NotEmpty(message = "title should not be empty or null")
    private String title;

    @NotEmpty(message = "link should not be empty or null")
    private String link;

    private String description;

    private Date pubDate;

    private String source;

    private String imageUrl;
}