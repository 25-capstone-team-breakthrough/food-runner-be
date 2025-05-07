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
  private String userId;  // 사용자 ID
  private LocalDateTime selectDate;  // 선택 날짜
  // MealLog 테이블
  private List<Long> mealIds;
  // ImageMealLog 테이블
  private List<String> mealLogImage;
  // SearchMealLog 테이블
  private List<String> foodLogImage;

  public static SelectDateMealLogDTO empty(String userId, LocalDateTime date) {
    SelectDateMealLogDTO dto = new SelectDateMealLogDTO();
    dto.setUserId(userId);
    dto.setSelectDate(date);
    dto.setMealIds(List.of());
    dto.setMealLogImage(List.of());
    dto.setFoodLogImage(List.of());

    return dto;
  }
}
