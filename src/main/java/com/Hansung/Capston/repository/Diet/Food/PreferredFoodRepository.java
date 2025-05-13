package com.Hansung.Capston.repository.Diet.Food;

import com.Hansung.Capston.entity.Diet.Food.FoodData;
import com.Hansung.Capston.entity.Diet.Food.PreferredFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferredFoodRepository extends JpaRepository<PreferredFood, Long> {
    List<PreferredFood> findByUserUserId(String userId);

    PreferredFood findByUserUserIdAndFoodFoodId(String user_userId, Long food_foodId);
}

