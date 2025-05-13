package com.Hansung.Capston.repository.Diet.Ingredient;


import com.Hansung.Capston.entity.Diet.Ingredient.RecommendedIngredient;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedIngredientRepository extends
    JpaRepository<RecommendedIngredient, Long> {

  List<RecommendedIngredient> findByUserUserId(String userId);


  void deleteAllByUserUserId(String userId);
}