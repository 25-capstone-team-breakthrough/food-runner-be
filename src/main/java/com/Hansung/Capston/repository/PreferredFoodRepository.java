package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.PreferredFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferredFoodRepository extends JpaRepository<PreferredFood, Long> {
    List<PreferredFood> findByUserUserId(String userId);
}

