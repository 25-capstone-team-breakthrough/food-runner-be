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
  private RecipeDataResponse recipeData;
  private DietType dietType;
  private DayOfWeek date;

  public static RecommendRecipeResponse toDto(RecommendedRecipe recommendedRecipe) {
    RecommendRecipeResponse response = new RecommendRecipeResponse();

    response.setRecommendedRecipeId(recommendedRecipe.getRecommendedRecipeId());
    response.setRecipeData(RecipeDataResponse.toDto(recommendedRecipe.getRecipeData()));
    response.setDietType(recommendedRecipe.getType());
    response.setDate(recommendedRecipe.getDate());

    return response;
  }
}
