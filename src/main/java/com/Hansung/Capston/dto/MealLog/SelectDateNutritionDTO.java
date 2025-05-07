package com.Hansung.Capston.dto.MealLog;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SelectDateNutritionDTO {
  private String userId;  // 사용자 ID
  private LocalDateTime selectDate;  // 선택 날짜

  private Double calories;
  private Double protein;
  private Double carbohydrate;
  private Double fat;
  private Double sugar;
  private Double sodium;
  private Double dietaryFiber;
  private Double calcium;
  private Double saturatedFat;
  private Double transFat;
  private Double cholesterol;
  private Double vitaminA;
  private Double vitaminB1;
  private Double vitaminC;
  private Double vitaminD;
  private Double vitaminE;
  private Double magnesium;
  private Double zinc;
  private Double lactium;
  private Double potassium;
  private Double lArginine;
  private Double omega3;

  public static SelectDateNutritionDTO empty(String userId, LocalDateTime selectDate) {
    SelectDateNutritionDTO dto = new SelectDateNutritionDTO();
    dto.setUserId(userId);
    dto.setSelectDate(selectDate);

    dto.setCalories(0.0);
    dto.setProtein(0.0);
    dto.setCarbohydrate(0.0);
    dto.setFat(0.0);
    dto.setSugar(0.0);
    dto.setSodium(0.0);
    dto.setDietaryFiber(0.0);
    dto.setCalcium(0.0);
    dto.setSaturatedFat(0.0);
    dto.setTransFat(0.0);
    dto.setCholesterol(0.0);
    dto.setVitaminA(0.0);
    dto.setVitaminB1(0.0);
    dto.setVitaminC(0.0);
    dto.setVitaminD(0.0);
    dto.setVitaminE(0.0);
    dto.setMagnesium(0.0);
    dto.setZinc(0.0);
    dto.setLactium(0.0);
    dto.setPotassium(0.0);
    dto.setLArginine(0.0);
    dto.setOmega3(0.0);

    return dto;
  }
}
