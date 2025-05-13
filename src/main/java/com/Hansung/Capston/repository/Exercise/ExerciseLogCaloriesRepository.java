package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseLogCalories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ExerciseLogCaloriesRepository extends JpaRepository <ExerciseLogCalories, Integer> {
    List<ExerciseLogCalories> findAllByExerciseLog_User_UserIdOrderByCreatedAtDesc(String userId);

    @Modifying
    void deleteByExerciseLog_LogId(Integer logId);
   }
