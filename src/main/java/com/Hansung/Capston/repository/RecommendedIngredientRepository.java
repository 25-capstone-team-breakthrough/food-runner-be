package com.Hansung.Capston.repository;


import com.Hansung.Capston.entity.RecommendedIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendedIngredientRepository extends
    JpaRepository<RecommendedIngredient, Long> {

}
