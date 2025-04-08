package com.Hansung.Capston.dto;

import com.Hansung.Capston.entity.MealType;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDietCreateDTO {
  private MealType type = MealType.search;
  private Double calories = 0.0;
  private Double protein = 0.0;
  private Double carbohydrate = 0.0;
  private Double fat = 0.0;
  private Double sugar = 0.0;
  private Double sodium = 0.0;
  private Double dietaryFiber = 0.0;
  private Double calcium = 0.0;
  private Double saturatedFat = 0.0;
  private Double transFat = 0.0;
  private Double cholesterol = 0.0;
  private Double vitaminA = 0.0;
  private Double vitaminB1 = 0.0;
  private Double vitaminC = 0.0;
  private Double vitaminD = 0.0;
  private Double vitaminE = 0.0;
  private Double magnesium = 0.0;
  private Double zinc = 0.0;
  private Double lactium = 0.0;
  private Double potassium = 0.0;
  private Double lArginine = 0.0;
  private Double omega3 = 0.0;
  private LocalDateTime date;


  private Long foodId;
}
