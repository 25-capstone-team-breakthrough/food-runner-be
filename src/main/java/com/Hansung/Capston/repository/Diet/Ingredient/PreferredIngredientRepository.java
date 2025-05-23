package com.Hansung.Capston.repository.Diet.Ingredient;

import com.Hansung.Capston.entity.Diet.Ingredient.PreferredIngredient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredIngredientRepository extends JpaRepository<PreferredIngredient, Long> {
  @Query("select p from PreferredIngredient p where p.user.userId = :userId and  p.ingredient.ingredientId = :ingredientId")
  Optional<PreferredIngredient> findByUserIdAndIngredientId(@Param("userId") String userId, @Param("ingredientId") Long ingredientId);

  List<PreferredIngredient> findByUserUserId(String userId);
}
