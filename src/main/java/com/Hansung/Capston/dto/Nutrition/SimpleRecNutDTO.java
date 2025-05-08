package com.Hansung.Capston.dto.Nutrition;

import com.Hansung.Capston.entity.RecommendedNutrient;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class SimpleRecNutDTO {
  private String type;
  private boolean inbodyInfo;
  private double calories;
  private double protein;
  private double carbohydrate;
  private double fat;
  private double sugar;
  private double saturatedFat;
  private double transFat;
  private double cholesterol;
  private double sodium;
  private double dietaryFiber;
  private double calcium;
  private double vitaminA;
  private double vitaminB1;
  private double vitaminC;
  private double vitaminD;
  private double vitaminE;
  private double magnesium;
  private double zinc;
  private double potassium;
  private double omega3;
  private double lactium;
  private double larginine;
  private String userId;

  public static SimpleRecNutDTO fromEntity(RecommendedNutrient entity) {
    return SimpleRecNutDTO.builder()
        .type(String.valueOf(entity.getType()))
        .inbodyInfo(entity.getInbodyInfo())
        .calories(entity.getCalories())
        .protein(entity.getProtein())
        .carbohydrate(entity.getCarbohydrate())
        .fat(entity.getFat())
        .sugar(entity.getSugar())
        .saturatedFat(entity.getSaturatedFat())
        .transFat(entity.getTransFat())
        .cholesterol(entity.getCholesterol())
        .sodium(entity.getSodium())
        .dietaryFiber(entity.getDietaryFiber())
        .calcium(entity.getCalcium())
        .vitaminA(entity.getVitaminA())
        .vitaminB1(entity.getVitaminB1())
        .vitaminC(entity.getVitaminC())
        .vitaminD(entity.getVitaminD())
        .vitaminE(entity.getVitaminE())
        .magnesium(entity.getMagnesium())
        .zinc(entity.getZinc())
        .potassium(entity.getPotassium())
        .omega3(entity.getOmega3())
        .lactium(entity.getLactium())
        .larginine(entity.getLArginine())
        .userId(entity.getUser().getUserId())
        .build();
  }

}
