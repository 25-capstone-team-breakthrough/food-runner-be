package com.Hansung.Capston.dto.Diet.Food;

import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Food.PreferredFood;
import lombok.Data;

@Data
public class PreferredFoodResponse {
  private Long id;
  private FoodData food;

  static public PreferredFoodResponse toDTO(PreferredFood preferredFoodResponse) {
    PreferredFoodResponse preferredFoodResponseDTO = new PreferredFoodResponse();
    preferredFoodResponseDTO.setId(preferredFoodResponse.getId());
    preferredFoodResponseDTO.setFood(preferredFoodResponse.getFood());

    return preferredFoodResponseDTO;
  }
}
