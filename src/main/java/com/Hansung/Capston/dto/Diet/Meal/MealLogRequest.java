package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.entity.DataSet.MealType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MealLogRequest {
  MealType type;
  String mealImage;
  Long foodId;
  LocalDateTime dateTime;
}
