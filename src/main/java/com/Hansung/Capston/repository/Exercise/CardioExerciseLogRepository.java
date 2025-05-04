package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.CardioExerciseLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardioExerciseLogRepository extends JpaRepository<CardioExerciseLog, Integer> {
}
