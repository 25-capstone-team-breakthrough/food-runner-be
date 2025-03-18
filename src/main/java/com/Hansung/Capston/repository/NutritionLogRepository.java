package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.NutritionLog;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
  // 유저 정보와 날짜를 이용해서 해당 날짜에 섭취한 영양소 및 칼로리 찾기
  @Query("SELECT n FROM NutritionLog n WHERE FUNCTION('DATE', n.date) = FUNCTION('DATE', :date) AND n.user.userId = :userId")
  NutritionLog findByDateAndUserId(@Param("date") LocalDateTime date, @Param("userId") String userId);
}

