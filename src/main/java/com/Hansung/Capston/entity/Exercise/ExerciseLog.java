package com.Hansung.Capston.entity.Exercise;

import com.Hansung.Capston.common.ExerciseType;
import com.Hansung.Capston.entity.UserInfo.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Exercise_log")
@Data
@NoArgsConstructor
@Getter
@Setter
public class ExerciseLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Integer logId;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;     // 읽기 전용 userId

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseData exerciseData;


    @Column(name = "exercise_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ExerciseType exerciseType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;



}
