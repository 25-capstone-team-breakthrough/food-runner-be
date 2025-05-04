package com.Hansung.Capston.entity;

import com.Hansung.Capston.common.ExerciseType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Exercise_log")
@Data
@NoArgsConstructor
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer log_id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "exercise_id", nullable = false)
    private Integer exerciseId;

    @Column(name = "exercise_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Column(name = "created_at", nullable = false, columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createdAt;

}
