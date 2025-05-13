package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.entity.Diet.Meal.SearchMealLog;
import lombok.Data;

@Data
public class SearchMealLogDTO {
  private Long searchMealLogId;
  private MealLogDTO mealLog;
  private Long foodId;
  private String foodName;
  private String foodImage;

  public static SearchMealLogDTO toDTO(SearchMealLog searchMealLog) {
    SearchMealLogDTO dto = new SearchMealLogDTO();
    dto.setSearchMealLogId(searchMealLog.getSearchMealLogId());
    dto.setMealLog(MealLogDTO.toDTO(searchMealLog.getMealLog()));
    dto.setFoodId(searchMealLog.getFoodId());
    dto.setFoodName(searchMealLog.getFoodName());
    dto.setFoodImage(searchMealLog.getFoodImage());

    return dto;
  }
}
