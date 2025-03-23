package com.Hansung.Capston.dto;

import com.Hansung.Capston.entity.FoodData;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FoodDataResponse {
  private Long foodId;
  private String foodImage;
  private String foodName;
  private String foodCompany;
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

  public static FoodDataResponse fromEntity(FoodData entity) {
    return FoodDataResponse.builder()
        .foodId(entity.getFoodId())
        .foodImage(entity.getFoodImage())
        .foodName(entity.getFoodName())
        .foodCompany(entity.getFoodCompany())
        .calories(entity.getCalories())
        .protein(entity.getProtein())
        .carbohydrate(entity.getCarbohydrate())
        .fat(entity.getFat())
        .sugar(entity.getSugar())
        .sodium(entity.getSodium())
        .dietaryFiber(entity.getDietaryFiber())
        .calcium(entity.getCalcium())
        .saturatedFat(entity.getSaturatedFat())
        .transFat(entity.getTransFat())
        .cholesterol(entity.getCholesterol())
        .vitaminA(entity.getVitaminA())
        .vitaminB1(entity.getVitaminB1())
        .vitaminC(entity.getVitaminC())
        .vitaminD(entity.getVitaminD())
        .vitaminE(entity.getVitaminE())
        .magnesium(entity.getMagnesium())
        .zinc(entity.getZinc())
        .lactium(entity.getLactium())
        .potassium(entity.getPotassium())
        .lArginine(entity.getLArginine())
        .omega3(entity.getOmega3())
        .build();
  }
}
