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



  public static FoodDataResponse toDto(FoodData foodData) {
    FoodDataResponse dto = new FoodDataResponse();

    // 기본 정보
    dto.setFoodId(foodData.getFoodId());
    dto.setFoodName(foodData.getFoodName());
    dto.setFoodCompany(foodData.getFoodCompany());
    dto.setFoodImage(foodData.getFoodImage());
    dto.setOneServing(foodData.getOneServing());

    double ratio = foodData.getOneServing() / 100.0;


    // 1인분 기준 환산 영양소
    dto.setCalories(round(foodData.getCalories() * ratio));
    dto.setProtein(round(foodData.getProtein() * ratio));
    dto.setFat(round(foodData.getFat() * ratio));
    dto.setCarbohydrate(round(foodData.getCarbohydrate() * ratio));
    dto.setSugar(round(foodData.getSugar() * ratio));
    dto.setDietaryFiber(round(foodData.getDietaryFiber() * ratio));
    dto.setCalcium(round(foodData.getCalcium() * ratio));
    dto.setPotassium(round(foodData.getPotassium() * ratio));
    dto.setSodium(round(foodData.getSodium() * ratio));
    dto.setVitaminA(round(foodData.getVitaminA() * ratio));
    dto.setVitaminB1(round(foodData.getVitaminB1() * ratio));
    dto.setVitaminC(round(foodData.getVitaminC() * ratio));
    dto.setVitaminD(round(foodData.getVitaminD() * ratio));
    dto.setCholesterol(round(foodData.getCholesterol() * ratio));
    dto.setSaturatedFat(round(foodData.getSaturatedFat() * ratio));
    dto.setTransFat(round(foodData.getTransFat() * ratio));
    dto.setVitaminE(round(foodData.getVitaminE() * ratio));
    dto.setZinc(round(foodData.getZinc() * ratio));
    dto.setMagnesium(round(foodData.getMagnesium() * ratio));
    dto.setLactium(round(foodData.getLactium() * ratio));
    dto.setOmega3(round(foodData.getOmega3() * ratio));
    dto.setLArginine(round(foodData.getLArginine() * ratio));

    return dto;
  }

  private static Double round(Double value) {
    return Math.round(value * 100.0) / 100.0;
  }
}
