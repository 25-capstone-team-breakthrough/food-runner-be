package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.MealLog.MealLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MealLogRepository extends JpaRepository<MealLog, Long> {
  // 특정 날짜에 특정 사용자가 먹은 식사 기록 검색

  @Query("SELECT m FROM MealLog m WHERE m.user.userId = :userId AND FUNCTION('DATE', m.date) = :date")
  List<MealLog> findByUserIdAndDateOnly(@Param("userId") String userId, @Param("date") LocalDate date);

}

