package com.newshunter.news_fetcher_service.utiltis;


import lombok.Getter;

@Getter
public enum RssPriorityConstraint {

    CRITICAL(1),
    HIGH(2),
    MEDIUM(3),
    LOW(4),
    VERY_LOW(5),
    ARCHIVE(6);

    private final int id;

    RssPriorityConstraint(int id) {
        this.id = id;
    }


}

