package com.Hansung.Capston.repository.Exercise;

import com.Hansung.Capston.entity.Exercise.StrengthExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StrengthExerciseLogRepository extends JpaRepository<StrengthExerciseLog, Integer> {
    List<StrengthExerciseLog> findAllByExerciseLog_LogId(Integer logId);
    void deleteByExerciseLog_LogId(Integer logId);
}
