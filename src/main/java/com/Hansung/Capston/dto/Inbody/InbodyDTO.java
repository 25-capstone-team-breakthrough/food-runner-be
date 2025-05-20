package com.Hansung.Capston.dto.Inbody;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter

public class InbodyDTO {
    private Integer pictureId;
    private Integer inbodyId;
    private String url;
    private LocalDateTime createdAt;

    public InbodyDTO(Integer pictureId, Integer inbodyId, String url, LocalDateTime createdAt) {
        this.pictureId = pictureId;
        this.inbodyId = inbodyId;
        this.url = url;
        this.createdAt = createdAt;
    }
}
