package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.entity.MealLog.ImageMealLog;
import com.Hansung.Capston.entity.MealLog.MealLog;
import com.Hansung.Capston.entity.MealLog.SearchMealLog;
import java.util.List;
import lombok.Data;

@Data
public class MealLogResponse {
  List<ImageMealLog> imageMealLogs;
  List<SearchMealLog> searchMealLogs;
}
