package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.PreferredFood;
import com.Hansung.Capston.entity.PreferredSupplement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferredFoodRepository extends JpaRepository<PreferredFood, Long> {
    List<PreferredFood> findByUserId(String userId);
}

