package com.Hansung.Capston.dto.Diet.Meal;

import com.Hansung.Capston.common.MealType;
import com.Hansung.Capston.entity.Diet.Meal.MealLog;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MealLogDTO {
  private Long mealId;
  private MealType type; // ENUM ('search', 'image')
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
  private LocalDateTime date;

  static public MealLogDTO toDTO(MealLog mealLog) {
    MealLogDTO dto = new MealLogDTO();
    dto.mealId = mealLog.getMealId();
    dto.type = mealLog.getType();
    dto.calories = mealLog.getCalories();
    dto.protein = mealLog.getProtein();
    dto.carbohydrate = mealLog.getCarbohydrate();
    dto.fat = mealLog.getFat();
    dto.sugar = mealLog.getSugar();
    dto.sodium = mealLog.getSodium();
    dto.dietaryFiber = mealLog.getDietaryFiber();
    dto.calcium = mealLog.getCalcium();
    dto.saturatedFat = mealLog.getSaturatedFat();
    dto.transFat = mealLog.getTransFat();
    dto.cholesterol = mealLog.getCholesterol();
    dto.vitaminA = mealLog.getVitaminA();
    dto.vitaminB1 = mealLog.getVitaminB1();
    dto.vitaminC = mealLog.getVitaminC();
    dto.vitaminD = mealLog.getVitaminD();
    dto.vitaminE = mealLog.getVitaminE();
    dto.magnesium = mealLog.getMagnesium();
    dto.zinc = mealLog.getZinc();
    dto.lactium = mealLog.getLactium();
    dto.potassium = mealLog.getPotassium();
    dto.lArginine = mealLog.getLArginine();
    dto.omega3 = mealLog.getOmega3();
    dto.date = mealLog.getDate();
    return dto;
  }
}
