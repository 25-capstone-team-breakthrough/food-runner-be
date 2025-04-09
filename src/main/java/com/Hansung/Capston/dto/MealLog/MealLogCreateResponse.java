package com.Hansung.Capston.dto.MealLog;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealLogCreateResponse {
  // Users 테이블
  private String userId;  // 사용자 ID
  // NutritionLog 테이블
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
  private LocalDateTime selectDate;

  // MealLog 테이블
  private List<Long> mealIds;
  // ImageMealLog 테이블
  private List<byte[]> mealLogImage;
  // SearchMealLog 테이블
  private List<String> foodLogImage;
  // PreferredFood 테이블
  private List<String> preferredFoodImage;
  // PreferredSupplement 테이블
  private List<String> preferredSupplementImage;

  public static MealLogCreateResponse empty(String userId, LocalDateTime selectDate) {
    MealLogCreateResponse dto = new MealLogCreateResponse();

    dto.setUserId(userId);
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
    dto.setSelectDate(selectDate);

    dto.setMealIds(List.of());
    dto.setMealLogImage(List.of());
    dto.setFoodLogImage(List.of());
    dto.setPreferredFoodImage(List.of());
    dto.setPreferredSupplementImage(List.of());

    return dto;
  }

}
