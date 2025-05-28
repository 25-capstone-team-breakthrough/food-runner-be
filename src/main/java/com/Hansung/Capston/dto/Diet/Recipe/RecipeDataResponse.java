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

    int serve = dto.calculateServe(recipeData);


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
    dto.setCalories(recipeData.getCalories()*serve);
    dto.setProtein(recipeData.getProtein()*serve);
    dto.setFat(recipeData.getFat()*serve);
    dto.setCarbohydrate(recipeData.getCarbohydrate()*serve);

    return dto;
  }

  private int calculateServe(RecipeData recipeData) {
    String servingString = recipeData.getServing();
    String numbersOnly = "";

    if (servingString != null) {
      numbersOnly = servingString.replaceAll("\\D", "").trim();
    }

    int servingValue = 0; // 기본값 설정

    if (!numbersOnly.isEmpty()) {
      try {
        servingValue = Integer.parseInt(numbersOnly);
      } catch (NumberFormatException e) {
        System.err.println("경고: recipeData.getServing()에서 숫자를 추출했지만 유효하지 않습니다: '" + servingString + "' -> '" + numbersOnly + "'");
      }
    } else {
      System.err.println("경고: recipeData.getServing()에서 숫자를 추출할 수 없거나 비어있습니다: '" + servingString + "'");
    }

    int serve;
    if (recipeData.getOneServing() != 0) {
      serve = (recipeData.getOneServing() / 100) * servingValue;
    } else {
      serve = 1;
    }

    return serve;
  }
}
