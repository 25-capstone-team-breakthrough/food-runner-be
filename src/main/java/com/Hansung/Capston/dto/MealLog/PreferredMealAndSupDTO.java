package com.Hansung.Capston.dto.MealLog;

import java.util.List;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PreferredMealAndSupDTO {
  private String userId;  // 사용자 ID
  // PreferredFood 테이블
  private List<String> preferredFoodImage;
  // PreferredSupplement 테이블
  private List<String> preferredSupplementImage;

  public static PreferredMealAndSupDTO empty(String userId) {
    PreferredMealAndSupDTO dto = new PreferredMealAndSupDTO();
    dto.setUserId(userId);
    dto.setPreferredFoodImage(List.of());
    dto.setPreferredSupplementImage(List.of());

    return dto;
  }
}
