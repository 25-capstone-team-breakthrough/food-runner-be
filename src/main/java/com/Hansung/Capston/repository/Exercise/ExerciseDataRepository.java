package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExerciseDataRepository extends JpaRepository<ExerciseData, Integer> {
    Optional<ExerciseData> findByExerciseName(String exerciseName);
}
