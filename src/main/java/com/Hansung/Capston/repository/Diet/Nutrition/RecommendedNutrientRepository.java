package com.Hansung.Capston.repository.Diet.Nutrition;

import com.Hansung.Capston.common.NutritionType;
import com.Hansung.Capston.entity.Diet.Nutrient.RecommendedNutrient;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedNutrientRepository extends JpaRepository<RecommendedNutrient, Long> {

  @Query("SELECT r FROM RecommendedNutrient r WHERE r.user.userId = :userId AND r.type = :type")
  Optional<RecommendedNutrient> findByUserIdAndType(@Param("userId") String userId, @Param("type") NutritionType type);

  List<RecommendedNutrient> findByUserUserId(String userId);
}
