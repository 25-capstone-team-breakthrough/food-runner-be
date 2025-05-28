package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExerciseDataRepository extends JpaRepository<ExerciseData, Integer> {
    Optional<ExerciseData> findByExerciseName(String exerciseName);


    @Query("""
      select e from ExerciseData e
      where lower(replace(e.exerciseName, ' ', '')) = :normalizedName
    """)
    Optional<ExerciseData> findByNormalizedName(@Param("normalizedName") String normalizedName);

}
