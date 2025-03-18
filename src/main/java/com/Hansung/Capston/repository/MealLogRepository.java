package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.ImageMealLog;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.entity.SearchMealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {

}

