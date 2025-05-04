package com.Hansung.Capston.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Strength_Exercise_Log")
@Data
@NoArgsConstructor
public class StrengthExerciseLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer strengthLogId;

    @OneToOne
    @JoinColumn(name = "exercise_log_id", nullable = false)
    private ExerciseLog exerciseLog;

    @Column(name = "sets")
    private Integer sets;

    @Column(name = "reps")
    private Integer reps;

    @Column(name = "weight")
    private Double weight;
}
