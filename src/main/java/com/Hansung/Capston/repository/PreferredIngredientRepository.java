package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.PreferredIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferredIngredientRepository extends JpaRepository<PreferredIngredient, Long> {

}
