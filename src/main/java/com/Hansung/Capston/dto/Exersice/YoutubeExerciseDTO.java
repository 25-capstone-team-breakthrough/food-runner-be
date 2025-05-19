package com.Hansung.Capston.dto.Exersice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YoutubeExerciseDTO {
    private String videoId;
    private String title;
    private String url;

    private String category;
    private Boolean isAIRecommendation;
}
