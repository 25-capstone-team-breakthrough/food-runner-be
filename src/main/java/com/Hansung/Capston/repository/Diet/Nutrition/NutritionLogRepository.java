package com.Hansung.Capston.repository.Diet.Nutrition;

import com.Hansung.Capston.dto.Diet.Nutrition.AverageNutritionDTO;
import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {


  @Query("""
    SELECT new com.Hansung.Capston.dto.Diet.Nutrition.AverageNutritionDTO(
        AVG(n.calories),
        AVG(n.protein),
        AVG(n.carbohydrate),
        AVG(n.fat),
        AVG(n.sugar),
        AVG(n.sodium),
        AVG(n.dietaryFiber),
        AVG(n.calcium),
        AVG(n.saturatedFat),
        AVG(n.transFat),
        AVG(n.cholesterol),
        AVG(n.vitaminA),
        AVG(n.vitaminB1),
        AVG(n.vitaminC),
        AVG(n.vitaminD),
        AVG(n.vitaminE),
        AVG(n.magnesium),
        AVG(n.zinc),
        AVG(n.lactium),
        AVG(n.potassium),
        AVG(n.lArginine),
        AVG(n.omega3)
    )
    FROM NutritionLog n
    WHERE n.user.userId = :userId
    AND n.date >= :startDate
""")
  AverageNutritionDTO findAverageNutritionForLast30Days(@Param("userId") String userId,
      @Param("startDate") LocalDateTime startDate);

  List<NutritionLog> findByUser_UserIdAndDate(String userId, LocalDate date);

  List<NutritionLog> findByUserUserId(String user_userId);
}

