package com.Hansung.Capston.repository.Diet.Recipe;

import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeDataRepository extends JpaRepository<RecipeData, Long> {

    @Query(value = "SELECT * FROM recipe_data r WHERE FIND_IN_SET(:ingredient, r.cleaned_ingredients) > 0", nativeQuery = true)
    List<RecipeData> findByIngredient(@Param("ingredient") String ingredient);



    RecipeData findByRecipeName(String name);
}
