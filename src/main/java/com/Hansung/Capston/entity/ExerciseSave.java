package com.Hansung.Capston.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Exercise_save")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseSave {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_save", nullable = false)
    private Integer exSaveId; // 즐겨찾기 고유 ID (PK)

    @Column(name = "user_id", nullable = false)
    private String userId; // JWT에서 추출한 사용자 ID

    @Column(name = "exercise_id", nullable = false)
    private Integer exerciseId; // 클라이언트 로컬에 있는 운동의 고유 ID
}
