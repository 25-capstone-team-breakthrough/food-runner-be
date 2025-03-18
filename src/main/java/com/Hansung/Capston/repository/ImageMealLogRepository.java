package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.ImageMealLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMealLogRepository extends JpaRepository<ImageMealLog, Long> {
    // mealid를 통해서 imagemeallog 찾긴
    @Query("SELECT im FROM ImageMealLog im WHERE im.mealLog.mealId = :mealid")
    ImageMealLog findByMealid(@Param("mealid") Long mealid);
}
