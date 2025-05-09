package com.Hansung.Capston.repository.Diet.Meal;

import com.Hansung.Capston.entity.Diet.Meal.SearchMealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchMealLogRepository extends JpaRepository<SearchMealLog, Long> {
    @Query("SELECT sm FROM SearchMealLog sm WHERE sm.mealLog.mealId = :mealId")
    SearchMealLog findByMealid(@Param("mealId") Long mealId);
}
