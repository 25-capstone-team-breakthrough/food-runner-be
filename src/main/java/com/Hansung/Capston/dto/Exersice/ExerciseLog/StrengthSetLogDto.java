package com.Hansung.Capston.dto.Exersice.ExerciseLog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrengthSetLogDto {
    private Integer sets;
    private Integer reps;
    private Double weight;
}
