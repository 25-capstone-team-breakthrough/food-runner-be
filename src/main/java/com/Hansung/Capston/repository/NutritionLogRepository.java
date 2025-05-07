package com.Hansung.Capston.repository;

import com.Hansung.Capston.dto.MealLog.AverageNutritionDTO;
import com.Hansung.Capston.entity.NutritionLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
  // 유저 정보와 날짜를 이용해서 해당 날짜에 섭취한 영양소 및 칼로리 찾기
  @Query("SELECT n FROM NutritionLog n WHERE FUNCTION('DATE', n.date) = FUNCTION('DATE', :date) AND n.user.userId = :userId")
  List<NutritionLog> findByDateAndUserId(@Param("date") LocalDateTime date, @Param("userId") String userId);

  @Query("""
    SELECT new com.Hansung.Capston.dto.MealLog.AverageNutritionDTO(
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

  @Query("SELECT n FROM NutritionLog n WHERE n.user = :userId AND FUNCTION('DATE', n.date) = :date")
  List<NutritionLog> findByDateOnly(@Param("userId") String userId, @Param("date") LocalDate date);

}

