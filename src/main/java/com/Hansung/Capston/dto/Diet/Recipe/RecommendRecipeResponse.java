package com.Hansung.Capston.dto.Diet.Recipe;

import com.Hansung.Capston.common.DietType;
import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import java.time.LocalDate;
import lombok.Data;

@Data
public class RecommendRecipeResponse {
  private Long recommendedRecipeId;
  private RecipeData recipeData;
  private DietType dietType;
  private LocalDate date;
}
