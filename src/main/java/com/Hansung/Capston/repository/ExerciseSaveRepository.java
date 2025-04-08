package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.ExerciseSave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseSaveRepository extends JpaRepository<ExerciseSave, Integer> {
    Optional<ExerciseSave> findByUserIdAndExerciseId(String userId, Integer exerciseId);
    //운동 조회 - 이거 나중에 수정 확인
    List<ExerciseSave> findAllByUserId(String userId);

}
