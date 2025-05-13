package com.Hansung.Capston.repository.Diet.Supplement;

import com.Hansung.Capston.entity.Diet.Supplement.PreferredSupplement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferredSupplementRepository extends JpaRepository<PreferredSupplement, Long> {
    List<PreferredSupplement> findByUserUserId(String userId);

  PreferredSupplement findByUserUserIdAndSupplementDataSupplementId(String userId, Long supplementId);
}
