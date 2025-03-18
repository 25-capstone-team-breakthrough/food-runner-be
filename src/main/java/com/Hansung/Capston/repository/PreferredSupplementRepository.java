package com.Hansung.Capston.repository;

import com.Hansung.Capston.entity.PreferredSupplement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferredSupplementRepository extends JpaRepository<PreferredSupplement, Long> {
    List<PreferredSupplement> findbyuserId(String userId);
}
