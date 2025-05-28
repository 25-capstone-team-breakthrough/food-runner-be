package com.Hansung.Capston.repository.Diet.Food;

import com.Hansung.Capston.entity.Diet.Food.FoodData;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, Long> {

  List<FoodData> findByFoodName(String foodName);

  boolean findByFoodImageIsEmpty(String foodImage);
}

