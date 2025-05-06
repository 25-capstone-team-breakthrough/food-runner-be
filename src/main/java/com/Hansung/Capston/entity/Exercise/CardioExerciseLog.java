package com.Hansung.Capston.entity.Exercise;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Cardio_Exercise_Log")
@Data
@NoArgsConstructor
public class CardioExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cardio_log_id")
    private Integer cardioLogId;

    @ManyToOne
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private ExerciseLog exerciseLog;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "time")
    private Integer time;

    @Column(name = "pace")
    private Double pace;
}
