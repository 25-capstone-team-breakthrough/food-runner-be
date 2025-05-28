package com.Hansung.Capston.dto.Diet.Recipe;

import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import lombok.Data;

@Data
public class RecipeDataResponse {
  private Long recipeId;
  private String recipeName;
  private int recommendedCount;
  private String ingredients;
  private String serving;
  private String recipeImage;
  private String recipe;
  private String cleanedIngredients;
  private String relatedRecipe1;
  private String relatedRecipe2;
  private String relatedRecipe3;

  private Integer oneServing; // 1인분 기준 단위 (g)

  private Double calories;
  private Double protein;
  private Double fat;
  private Double carbohydrate;

  public static RecipeDataResponse toDto(RecipeData recipeData) {
    RecipeDataResponse dto = new RecipeDataResponse();

    dto.setRecipeId(recipeData.getRecipeId());
    dto.setRecipeName(recipeData.getRecipeName());
    dto.setRecommendedCount(recipeData.getRecommendedCount());
    dto.setIngredients(recipeData.getIngredients());
    dto.setServing(recipeData.getServing());
    dto.setRecipeImage(recipeData.getRecipeImage());
    dto.setRecipe(recipeData.getRecipe());
    dto.setCleanedIngredients(recipeData.getCleanedIngredients());
    dto.setRelatedRecipe1(recipeData.getRelatedRecipe1());
    dto.setRelatedRecipe2(recipeData.getRelatedRecipe2());
    dto.setRelatedRecipe3(recipeData.getRelatedRecipe3());
    dto.setOneServing(recipeData.getOneServing());
    dto.setCalories(recipeData.getCalories()*(recipeData.getOneServing()/100.0));
    dto.setProtein(recipeData.getProtein()*(recipeData.getOneServing()/100.0));
    dto.setFat(recipeData.getFat()*(recipeData.getOneServing()/100.0));
    dto.setCarbohydrate(recipeData.getCarbohydrate()*(recipeData.getOneServing()/100.0));

    return dto;
  }
}
