package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseLogCalories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseLogCaloriesRepository extends JpaRepository <ExerciseLogCalories, Integer> {
    List<ExerciseLogCalories> findAllByExerciseLog_User_UserIdOrderByCreatedAtDesc(String userId);

}
