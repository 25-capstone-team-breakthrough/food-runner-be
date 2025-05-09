package com.Hansung.Capston.repository.Diet.Ingredient;

import com.Hansung.Capston.entity.Diet.Ingredient.IngredientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientDataRepository extends JpaRepository<IngredientData, Long> {

}
