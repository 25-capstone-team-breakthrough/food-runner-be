package com.Hansung.Capston.dto;

import java.time.LocalDateTime;

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

  // ImageMealLog 테이블
  private String[] mealLogImage;
  // SearchMealLog 테이블
  private String[] foodLogImage;
  // PreferredFood 테이블
  private String[] preferredFoodImage;
  // PreferredSupplement 테이블
  private String[] preferredSupplementImage;
  // FoodData 테이블
  private String[] foodDataImage;
  private String[] foodDataName;
  private String[] foodDataCompany;
  private String[] foodDataCalories;
  // SupplementData 테이블
  private String[] supplementDataImage;
  private String[] supplementDataName;
  private String[] supplementDataCompany;
  private String[] baseStandard;

}
