package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.entity.Diet.Meal.ImageMealLog;
import com.Hansung.Capston.entity.Diet.Meal.SearchMealLog;
import java.util.List;
import lombok.Data;

@Data
public class MealLogResponse {
  List<ImageMealLog> imageMealLogs;
  List<SearchMealLog> searchMealLogs;
}
