package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.NutritionType;
import com.Hansung.Capston.entity.RecommendedIngredient;
import com.Hansung.Capston.entity.RecommendedNutrient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedNutrientRepository extends JpaRepository<RecommendedNutrient, Long> {

  RecommendedNutrient findByUserUserIdAndType(String userId, NutritionType nutritionType);
}
