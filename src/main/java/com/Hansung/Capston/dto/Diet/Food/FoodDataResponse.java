package com.Hansung.Capston.dto.Diet.Food;

import com.Hansung.Capston.entity.Diet.Food.FoodData;
import lombok.Data;

@Data
public class FoodDataResponse {
  private Long foodId;
  private String foodName;
  private String foodCompany;
  private String foodImage;
  private Integer oneServing;

  private Double calories;
  private Double protein;
  private Double fat;
  private Double carbohydrate;
  private Double sugar;
  private Double dietaryFiber;
  private Double calcium;
  private Double potassium;
  private Double sodium;
  private Double vitaminA;
  private Double vitaminB1;
  private Double vitaminC;
  private Double vitaminD;
  private Double cholesterol;
  private Double saturatedFat;
  private Double transFat;
  private Double vitaminE;
  private Double zinc;
  private Double magnesium;
  private Double lactium;
  private Double omega3;
  private Double lArginine;

  private Double caloriesPerServing;
  private Double proteinPerServing;
  private Double fatPerServing;
  private Double carbohydratePerServing;
  private Double sugarPerServing;
  private Double dietaryFiberPerServing;
  private Double calciumPerServing;
  private Double potassiumPerServing;
  private Double sodiumPerServing;
  private Double vitaminAPerServing;
  private Double vitaminB1PerServing;
  private Double vitaminCPerServing;
  private Double vitaminDPerServing;
  private Double cholesterolPerServing;
  private Double saturatedFatPerServing;
  private Double transFatPerServing;
  private Double vitaminEPerServing;
  private Double zincPerServing;
  private Double magnesiumPerServing;
  private Double lactiumPerServing;
  private Double omega3PerServing;
  private Double lArgininePerServing;

  public static FoodDataResponse toDto(FoodData foodData) {
    FoodDataResponse dto = new FoodDataResponse();

    // 기본 정보
    dto.setFoodId(foodData.getFoodId());
    dto.setFoodName(foodData.getFoodName());
    dto.setFoodCompany(foodData.getFoodCompany());
    dto.setFoodImage(foodData.getFoodImage());
    dto.setOneServing(foodData.getOneServing());

    double ratio = foodData.getOneServing() / 100.0;

    // 100g 기준 영양소
    dto.setCalories(foodData.getCalories());
    dto.setProtein(foodData.getProtein());
    dto.setFat(foodData.getFat());
    dto.setCarbohydrate(foodData.getCarbohydrate());
    dto.setSugar(foodData.getSugar());
    dto.setDietaryFiber(foodData.getDietaryFiber());
    dto.setCalcium(foodData.getCalcium());
    dto.setPotassium(foodData.getPotassium());
    dto.setSodium(foodData.getSodium());
    dto.setVitaminA(foodData.getVitaminA());
    dto.setVitaminB1(foodData.getVitaminB1());
    dto.setVitaminC(foodData.getVitaminC());
    dto.setVitaminD(foodData.getVitaminD());
    dto.setCholesterol(foodData.getCholesterol());
    dto.setSaturatedFat(foodData.getSaturatedFat());
    dto.setTransFat(foodData.getTransFat());
    dto.setVitaminE(foodData.getVitaminE());
    dto.setZinc(foodData.getZinc());
    dto.setMagnesium(foodData.getMagnesium());
    dto.setLactium(foodData.getLactium());
    dto.setOmega3(foodData.getOmega3());
    dto.setLArginine(foodData.getLArginine());

    // 1인분 기준 환산 영양소
    dto.setCaloriesPerServing(round(foodData.getCalories() * ratio));
    dto.setProteinPerServing(round(foodData.getProtein() * ratio));
    dto.setFatPerServing(round(foodData.getFat() * ratio));
    dto.setCarbohydratePerServing(round(foodData.getCarbohydrate() * ratio));
    dto.setSugarPerServing(round(foodData.getSugar() * ratio));
    dto.setDietaryFiberPerServing(round(foodData.getDietaryFiber() * ratio));
    dto.setCalciumPerServing(round(foodData.getCalcium() * ratio));
    dto.setPotassiumPerServing(round(foodData.getPotassium() * ratio));
    dto.setSodiumPerServing(round(foodData.getSodium() * ratio));
    dto.setVitaminAPerServing(round(foodData.getVitaminA() * ratio));
    dto.setVitaminB1PerServing(round(foodData.getVitaminB1() * ratio));
    dto.setVitaminCPerServing(round(foodData.getVitaminC() * ratio));
    dto.setVitaminDPerServing(round(foodData.getVitaminD() * ratio));
    dto.setCholesterolPerServing(round(foodData.getCholesterol() * ratio));
    dto.setSaturatedFatPerServing(round(foodData.getSaturatedFat() * ratio));
    dto.setTransFatPerServing(round(foodData.getTransFat() * ratio));
    dto.setVitaminEPerServing(round(foodData.getVitaminE() * ratio));
    dto.setZincPerServing(round(foodData.getZinc() * ratio));
    dto.setMagnesiumPerServing(round(foodData.getMagnesium() * ratio));
    dto.setLactiumPerServing(round(foodData.getLactium() * ratio));
    dto.setOmega3PerServing(round(foodData.getOmega3() * ratio));
    dto.setLArgininePerServing(round(foodData.getLArginine() * ratio));

    return dto;
  }

  private static Double round(Double value) {
    return Math.round(value * 100.0) / 100.0;
  }
}
