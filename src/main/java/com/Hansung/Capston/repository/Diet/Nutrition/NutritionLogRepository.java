package com.Hansung.Capston.repository.Diet.Nutrition;

import com.Hansung.Capston.entity.Diet.Nutrient.NutritionLog;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {


  List<NutritionLog> findByUserUserId(String user_userId);

  Optional<NutritionLog> findByUser_UserIdAndDate(String userId, LocalDate onlyDate);

  @Query(value = "select * from nutrition_log n where n.user_id = :userId and n.date = :onlydate", nativeQuery = true)
  NutritionLog findByUserIdAndDate(@Param("userId") String userId, @Param("onlydate") LocalDate onlydate);
}
