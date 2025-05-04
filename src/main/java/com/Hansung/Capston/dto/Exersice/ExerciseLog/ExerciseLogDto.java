package com.Hansung.Capston.dto.Exersice;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExerciseLogDto {
    private Integer logId;
    private Integer exerciseId;

    private LocalDateTime createdAt;

    //유산소
    private Double distance;
    private Integer time;
    private Double pace;

    //근력
    private Integer sets;
    private Integer reps;
    private Double weight;
}
