package com.Hansung.Capston.repository.Diet.Nutrition;

import com.Hansung.Capston.dto.Diet.Nutrition.AverageNutritionDTO;
import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {


  Optional<NutritionLog> findByUser_UserIdAndDate(String userId, LocalDate date);

  List<NutritionLog> findByUserUserId(String user_userId);
}

