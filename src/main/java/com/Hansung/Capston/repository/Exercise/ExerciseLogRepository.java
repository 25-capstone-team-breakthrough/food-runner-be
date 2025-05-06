package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.ExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseLogRepository extends JpaRepository<ExerciseLog, Integer> {
    List<ExerciseLog> findAllByUser_UserIdOrderByCreatedAtDesc(String userId);

    Optional<ExerciseLog> findByLogIdAndUser_UserId(Integer logId, String userId);

}
