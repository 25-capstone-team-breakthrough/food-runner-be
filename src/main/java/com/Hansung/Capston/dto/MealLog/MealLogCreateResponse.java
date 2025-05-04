package com.Hansung.Capston.dto.MealLog;

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


  public static MealLogCreateResponse empty(String userId, LocalDateTime selectDate) {
    MealLogCreateResponse dto = new MealLogCreateResponse();

    dto.setUserId(userId);
    dto.setSelectDate(selectDate);

    dto.setSelectDateMealLog(SelectDateMealLogDTO.empty(userId, selectDate));
    dto.setSelectDateNutrition(SelectDateNutritionDTO.empty(userId, selectDate));
    dto.setRecommendationCalories(0);

    return dto;
  }

}
