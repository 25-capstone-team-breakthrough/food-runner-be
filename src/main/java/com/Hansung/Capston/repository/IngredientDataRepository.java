package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.DataSet.IngredientData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientDataRepository extends JpaRepository<IngredientData, Long> {

}
