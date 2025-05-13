package com.Hansung.Capston.repository.Diet.Recipe;

import com.Hansung.Capston.entity.Diet.Recipe.RecipeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeDataRepository extends JpaRepository<RecipeData, Long> {

}
