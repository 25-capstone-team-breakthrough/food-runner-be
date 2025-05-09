package com.Hansung.Capston.dto.MealLog;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SelectDateMealLogDTO {
  // MealLog 테이블
  private List<Long> mealIds;
  // ImageMealLog 테이블
  private List<String> mealLogImage;
  // SearchMealLog 테이블
  private List<String> foodLogImage;

}
