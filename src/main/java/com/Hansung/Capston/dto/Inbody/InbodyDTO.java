package com.Hansung.Capston.dto.Inbody;

import lombok.Getter;

@Getter

public class InbodyDTO {
    private Integer pictureId;
    private Integer inbodyId;
    private String url;

    public InbodyDTO(Integer pictureId, Integer inbodyId, String url) {
        this.pictureId = pictureId;
        this.inbodyId = inbodyId;
        this.url = url;
    }
}
