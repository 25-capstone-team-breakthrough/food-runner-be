package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.NutritionLog;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
  Optional<NutritionLog> findByDateAndUserId(LocalDate date, String userId);
}
