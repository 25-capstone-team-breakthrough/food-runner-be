package com.Hansung.Capston.entity.Exercise;

import com.Hansung.Capston.entity.UserInfo.User;
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

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;     // 읽기 전용 userId

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // User 엔티티와는 N:1 관계만 매핑

    @Column(name = "exercise_id", nullable = false)
    private Integer exerciseId; // 클라이언트 로컬에 있는 운동의 고유 ID


}
