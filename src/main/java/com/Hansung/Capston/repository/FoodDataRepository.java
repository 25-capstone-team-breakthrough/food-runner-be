package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.FoodData;
import com.Hansung.Capston.entity.SupplementData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodDataRepository extends JpaRepository<FoodData, Long> {

}

