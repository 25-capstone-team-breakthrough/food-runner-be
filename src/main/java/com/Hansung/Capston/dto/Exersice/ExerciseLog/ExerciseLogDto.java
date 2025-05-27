package com.Hansung.Capston.dto.Exersice.ExerciseLog;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExerciseLogDto {
    private Integer logId;
    private Integer exerciseId;
    private String exerciseName;

    private LocalDateTime createdAt;

    //유산소
    private Double distance;
    private Integer time;
    private Double pace;

    // 근력 기록: 여러 세트를 담을 리스트
    private List<StrengthSetLogDto> strengthSets;
}
