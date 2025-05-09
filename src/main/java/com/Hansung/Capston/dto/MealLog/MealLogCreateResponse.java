package com.Hansung.Capston.dto.MealLog;

import com.Hansung.Capston.dto.Nutrition.NutritionStatusDTO;
import com.Hansung.Capston.dto.Nutrition.RecommendedNutrientDTO;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealLogCreateResponse {
  // Users 테이블
  private String userId;  // 사용자 ID
  private LocalDateTime selectDate;
  private SelectDateMealLogDTO selectDateMealLog;
  private SelectDateNutritionDTO selectDateNutrition;
  private double recommendationCalories;
  private NutritionStatusDTO nutritionStatus;
  private RecommendedNutrientDTO recommendedNutrientDTO;

}
