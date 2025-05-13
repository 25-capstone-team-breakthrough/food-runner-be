package com.Hansung.Capston.dto.Diet.Nutrition;

import com.Hansung.Capston.common.NutritionType;
import com.Hansung.Capston.entity.Diet.Nutrient.RecommendedNutrient;
import lombok.Data;

@Data
public class RecommendedNutrientResponse {
  private Integer id;
  private NutritionType type; // MIN, MAX
  private Boolean inbodyInfo = false;
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

  public static RecommendedNutrientResponse toDto(RecommendedNutrient recommendedNutrient) {
    RecommendedNutrientResponse response = new RecommendedNutrientResponse();
    response.id = recommendedNutrient.getId();
    response.type = recommendedNutrient.getType();
    response.inbodyInfo = recommendedNutrient.getInbodyInfo();
    response.calories = recommendedNutrient.getCalories();
    response.protein = recommendedNutrient.getProtein();
    response.carbohydrate = recommendedNutrient.getCarbohydrate();
    response.fat = recommendedNutrient.getFat();
    response.sugar = recommendedNutrient.getSugar();
    response.sodium = recommendedNutrient.getSodium();
    response.dietaryFiber = recommendedNutrient.getDietaryFiber();
    response.calcium = recommendedNutrient.getCalcium();
    response.saturatedFat = recommendedNutrient.getSaturatedFat();
    response.transFat = recommendedNutrient.getTransFat();
    response.cholesterol = recommendedNutrient.getCholesterol();
    response.vitaminA = recommendedNutrient.getVitaminA();
    response.vitaminB1 = recommendedNutrient.getVitaminB1();
    response.vitaminC = recommendedNutrient.getVitaminC();
    response.vitaminD = recommendedNutrient.getVitaminD();
    response.vitaminE = recommendedNutrient.getVitaminE();
    response.magnesium = recommendedNutrient.getMagnesium();
    response.zinc = recommendedNutrient.getZinc();
    response.lactium = recommendedNutrient.getLactium();
    response.potassium = recommendedNutrient.getPotassium();
    response.lArginine = recommendedNutrient.getLArginine();
    response.omega3 = recommendedNutrient.getOmega3();
    return response;
  }
}