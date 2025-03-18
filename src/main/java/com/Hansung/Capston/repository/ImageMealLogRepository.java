package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.ImageMealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMealLogRepository extends JpaRepository<ImageMealLog, Long> {

}
