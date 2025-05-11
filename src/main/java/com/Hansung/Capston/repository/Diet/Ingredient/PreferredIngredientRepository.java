package com.Hansung.Capston.repository.Diet.Ingredient;

import com.Hansung.Capston.entity.Diet.Ingredient.PreferredIngredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredIngredientRepository extends JpaRepository<PreferredIngredient, Long> {
  List<PreferredIngredient> findByUserUserId(String userId);
}
