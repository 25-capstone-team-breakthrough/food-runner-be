package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.ImageMealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMealLogRepository extends JpaRepository<ImageMealLog, Long> {
    // mealid를 통해서 imagemeallog 찾긴
    @Query("SELECT im FROM ImageMealLog im WHERE im.mealLog.mealId = :mealid")
    ImageMealLog findByMealId(@Param("mealid") Long mealid);
}
