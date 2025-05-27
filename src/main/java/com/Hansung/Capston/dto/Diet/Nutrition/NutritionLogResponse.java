package com.Hansung.Capston.dto.Diet.Nutrition;

import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class NutritionLogResponse {
  private Long nutritionLogId;
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
  private LocalDate date;

  public static NutritionLogResponse toDto(NutritionLog nutritionLog) {
    NutritionLogResponse dto = new NutritionLogResponse();
    dto.nutritionLogId = nutritionLog.getNutritionLogId();
    dto.calories = nutritionLog.getCalories();
    dto.protein = nutritionLog.getProtein();
    dto.carbohydrate = nutritionLog.getCarbohydrate();
    dto.fat = nutritionLog.getFat();
    dto.sugar = nutritionLog.getSugar();
    dto.sodium = nutritionLog.getSodium();
    dto.dietaryFiber = nutritionLog.getDietaryFiber();
    dto.calcium = nutritionLog.getCalcium();
    dto.saturatedFat = nutritionLog.getSaturatedFat();
    dto.transFat = nutritionLog.getTransFat();
    dto.cholesterol = nutritionLog.getCholesterol();
    dto.vitaminA = nutritionLog.getVitaminA();
    dto.vitaminB1 = nutritionLog.getVitaminB1();
    dto.vitaminC = nutritionLog.getVitaminC();
    dto.vitaminD = nutritionLog.getVitaminD();
    dto.vitaminE = nutritionLog.getVitaminE();
    dto.magnesium = nutritionLog.getMagnesium();
    dto.zinc = nutritionLog.getZinc();
    dto.lactium = nutritionLog.getLactium();
    dto.potassium = nutritionLog.getPotassium();
    dto.lArginine = nutritionLog.getLArginine();
    dto.omega3 = nutritionLog.getOmega3();
    dto.date = nutritionLog.getDate();
    return dto;
  }
}