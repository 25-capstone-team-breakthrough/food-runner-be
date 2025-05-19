package com.Hansung.Capston.dto.Diet.Recipe;

import com.Hansung.Capston.common.DayOfWeek;
import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import java.time.LocalDate;

import com.Hansung.Capston.entity.Diet.Recipe.RecommendedRecipe;
import lombok.Data;

@Data
public class RecommendRecipeResponse {
  private Long recommendedRecipeId;
  private RecipeData recipeData;
  private DietType dietType;
  private DayOfWeek date;

  public static RecommendRecipeResponse toDto(RecommendedRecipe recommendedRecipe) {
    RecommendRecipeResponse response = new RecommendRecipeResponse();

    response.setRecommendedRecipeId(response.getRecommendedRecipeId());
    response.setRecipeData(recommendedRecipe.getRecipeData());
    response.setDietType(response.getDietType());
    response.setDate(recommendedRecipe.getDate());

    return response;
  }
}
