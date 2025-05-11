package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.common.MealType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MealLogRequest {
  MealType type;
  String mealImage;
  Long foodId;
  LocalDate date;
}
