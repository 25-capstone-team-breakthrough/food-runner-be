package com.Hansung.Capston.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DietCreateWindowDTO {
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
  private List<String> mealLogImage;
  // SearchMealLog 테이블
  private List<String> foodLogImage;
  // PreferredFood 테이블
  private List<String> preferredFoodImage;
  // PreferredSupplement 테이블
  private List<String> preferredSupplementImage;
}
