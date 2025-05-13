package com.Hansung.Capston.dto.Diet.Ingredient;

import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
import com.Hansung.Capston.entity.Diet.Ingredient.PreferredIngredient;
import lombok.Data;

@Data
public class PreferredIngredientResponse {
  private Integer id;
  private IngredientData ingredient;

  public static PreferredIngredientResponse toDTO(PreferredIngredient preferredIngredient) {
    PreferredIngredientResponse response = new PreferredIngredientResponse();
    response.setId(preferredIngredient.getId());
    response.setIngredient(preferredIngredient.getIngredient());

    return response;
  }
}
