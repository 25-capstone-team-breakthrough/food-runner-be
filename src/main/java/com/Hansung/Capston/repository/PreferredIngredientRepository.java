package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.PreferredIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredIngredientRepository extends JpaRepository<PreferredIngredient, Long> {
  @Query("select p from PreferredIngredient p where p.id = :userId and  p.ingredient = : ingredientId")
  PreferredIngredient findByUserIdAndIngredientId(@Param("userId") String userId, @Param("ingredientId") Long ingredientId);
}
