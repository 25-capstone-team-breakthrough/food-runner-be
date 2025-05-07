package com.Hansung.Capston.dto.Exersice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YoutubeExerciseDTO {
    private String videoId;
    private String title;
    private String url;
}
