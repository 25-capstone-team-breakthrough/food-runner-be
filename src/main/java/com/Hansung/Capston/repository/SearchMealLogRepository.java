package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.SearchMealLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchMealLogRepository extends JpaRepository<SearchMealLog, Long> {

}
