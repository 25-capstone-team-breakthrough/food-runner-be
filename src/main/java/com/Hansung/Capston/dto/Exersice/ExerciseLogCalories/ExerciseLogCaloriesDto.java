package com.Hansung.Capston.dto.Exersice.ExerciseLogCalories;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseLogCaloriesDto {
    private Integer calorieLogId;
    private Integer exerciseLogId;
    private Integer caloriesBurned;
    private LocalDateTime createdAt;
}
