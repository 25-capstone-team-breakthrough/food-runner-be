package com.Hansung.Capston.entity.Exercise;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Exercise_Log_Calories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExerciseLogCalories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer calorieLogId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_log_id", nullable = false, unique = true)
    private ExerciseLog exerciseLog;

    @Column(nullable = false)
    private Integer caloriesBurned;

    @Column(nullable = false, updatable = false,
            columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;
}
