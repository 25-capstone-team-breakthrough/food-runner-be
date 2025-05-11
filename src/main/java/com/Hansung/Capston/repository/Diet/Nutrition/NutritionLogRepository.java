package com.Hansung.Capston.repository.Diet.Nutrition;

import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {


  List<NutritionLog> findByUserUserId(String user_userId);

  Optional<NutritionLog> findByUser_UserIdAndDate(String userId, LocalDate onlyDate);
}

