package com.Hansung.Capston.repository.Diet.Recipe;

import com.Hansung.Capston.dto.Diet.Ingredient.RecommendedIngredientResponse;
import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeDataRepository extends JpaRepository<RecipeData, Long> {

    @Query("SELECT r FROM RecipeData r WHERE " +
            "FUNCTION('FIND_IN_SET', :ingredient1, r.cleanedIngredients) > 0 OR " +
            "FUNCTION('FIND_IN_SET', :ingredient2, r.cleanedIngredients) > 0 OR " +
            // ... (pref나 rec 리스트의 모든 재료에 대해 추가)
            "FUNCTION('FIND_IN_SET', :ingredientN, r.cleanedIngredients) > 0")
    List<RecipeData> findByIngredientsContainingAny(@Param("ingredientNames") List<String> ingredientNames);

    RecipeData findByRecipeName(String name);
}
