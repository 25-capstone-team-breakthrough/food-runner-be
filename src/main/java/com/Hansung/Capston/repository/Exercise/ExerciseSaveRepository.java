package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseSaveRepository extends JpaRepository<ExerciseSave, Integer> {
    Optional<ExerciseSave> findByUserIdAndExerciseId(String userId, Integer exerciseId);

    /// 사용자 ID로 즐겨찾기 목록 조회
    List<ExerciseSave> findAllByUserId(String userId);

}
