package com.Hansung.Capston.dto.Diet.Ingredient;

import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
import com.Hansung.Capston.entity.Diet.Ingredient.PreferredIngredient;
import com.Hansung.Capston.entity.Diet.Ingredient.RecommendedIngredient;
import lombok.Data;

@Data
public class RecommendedIngredientResponse {
  private Long id;
  private IngredientData ingredient;

  public static RecommendedIngredientResponse toDTO(RecommendedIngredient recommendedIngredient) {
    RecommendedIngredientResponse response = new RecommendedIngredientResponse();
    response.setId(recommendedIngredient.getId());
    response.setIngredient(recommendedIngredient.getIngredient());

    return response;
  }
}
