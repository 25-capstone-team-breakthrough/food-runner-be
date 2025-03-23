package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.ImageMealLog;
import com.Hansung.Capston.entity.MealLog;
import com.Hansung.Capston.entity.SearchMealLog;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
  // 특정 날짜에 특정 사용자가 먹은 식사 기록 검색
  @Query("SELECT m from MealLog m where FUNCTION('DATE', m.date) = FUNCTION('DATE', :date) AND m.user.userId = :userId")
  List<MealLog> findMealLogsByUserIdAndDate(String userId,LocalDateTime date);
}

