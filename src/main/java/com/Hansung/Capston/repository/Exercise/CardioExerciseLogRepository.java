package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.CardioExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardioExerciseLogRepository extends JpaRepository<CardioExerciseLog, Integer> {
    Optional<CardioExerciseLog> findByExerciseLog_LogId(Integer logId);
    void deleteByExerciseLog_LogId(Integer logId);
}
