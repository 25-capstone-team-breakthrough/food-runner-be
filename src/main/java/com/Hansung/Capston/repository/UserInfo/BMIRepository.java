package com.Hansung.Capston.repository.UserInfo;

import com.Hansung.Capston.entity.UserInfo.BMI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BMIRepository extends JpaRepository<BMI, String> {
}
