package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.BMI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BMIRepository extends JpaRepository<BMI, String> {
}
