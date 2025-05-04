package com.Hansung.Capston.entity;

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
    private Integer cardioLogId;

    @OneToOne
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private ExerciseLog exerciseLog;

    @Column(name = "distance")
    private Double distance;

    @Column(name = "time")
    private Integer time;

    @Column(name = "pace")
    private Double pace;
}
