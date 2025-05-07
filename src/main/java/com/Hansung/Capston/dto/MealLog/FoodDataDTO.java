package com.Hansung.Capston.dto.MealLog;

import com.Hansung.Capston.entity.FoodData;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FoodDataDTO {
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

  public static FoodDataDTO fromEntity(FoodData entity) {
    return FoodDataDTO.builder()
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

  public FoodData toEntity() {
    return FoodData.builder()
        .foodName(this.foodName)
        .calories(this.calories)
        .protein(this.protein)
        .carbohydrate(this.carbohydrate)
        .fat(this.fat)
        .sugar(this.sugar)
        .sodium(this.sodium)
        .dietaryFiber(this.dietaryFiber)
        .calcium(this.calcium)
        .saturatedFat(this.saturatedFat)
        .transFat(this.transFat)
        .cholesterol(this.cholesterol)
        .vitaminA(this.vitaminA)
        .vitaminB1(this.vitaminB1)
        .vitaminC(this.vitaminC)
        .vitaminD(this.vitaminD)
        .vitaminE(this.vitaminE)
        .magnesium(this.magnesium)
        .zinc(this.zinc)
        .lactium(this.lactium)
        .potassium(this.potassium)
        .lArginine(this.lArginine)
        .omega3(this.omega3)
        .build();
  }
}
